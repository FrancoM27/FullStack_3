import React, { useState, useEffect } from 'react';
// import ResenasProducto from '../resenas/ResenasProducto';

const DetalleCatalogo = ({ productoId, alVolver, rol }) => {
    const [producto, setProducto] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState('');
    const [cantidad, setCantidad] = useState(1);
    const [esperandoPago, setEsperandoPago] = useState(false);
    const [idPagoGenerado, setIdPagoGenerado] = useState(null);

    const token = sessionStorage.getItem('token');

    useEffect(() => { obtenerDetalle(); }, [productoId]);

    const obtenerDetalle = async () => {
        try {
            setCargando(true);
            const response = await fetch(`http://localhost:9000/api/productos/${productoId}`);
            if (!response.ok) throw new Error('No se pudo cargar el producto');
            const data = await response.json();
            setProducto(data);
        } catch (err) {
            setError('Error de conexión al cargar el detalle.');
        } finally {
            setCargando(false);
        }
    };

    const handleAgregarCarrito = () => {
        // Lógica que Esther o tú implementarán luego
        alert(`¡Agregaste ${cantidad} ${producto.nombre} al carrito! 🛒`);
    };

    const handleComprarAhora = async () => {
        if (!token) return alert("Inicia sesión para comprar.");

        const solicitudPago = {
            productoId: producto.id,
            cantidad: cantidad,
            monto: producto.precio * cantidad,
            descripcion: `Compra de ${cantidad}x ${producto.nombre}`
        };

        try {
            const response = await fetch('http://localhost:9000/api/pagos/iniciar', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
                body: JSON.stringify(solicitudPago)
            });

            if (response.ok) {
                const data = await response.json();
                if (data.transaccionId) {
                    setIdPagoGenerado(data.idPago);
                    setEsperandoPago(true);
                    window.open(data.transaccionId, '_blank');
                }
            }
        } catch (err) { alert("Error al conectar con Pagos."); }
    };

    const confirmarPagoManual = async () => {
        // En una app real, esto consultaría el estado real en Mercado Pago
        try {
            const response = await fetch(`http://localhost:9000/api/pagos/confirmar/${idPagoGenerado}`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                alert("¡Pago verificado y stock actualizado! 🎉");
                window.location.reload();
            }
        } catch (err) { alert("Error al confirmar."); }
    };

    if (cargando) return <p style={{ color: '#00d4ff', textAlign: 'center' }}>Cargando detalles...</p>;
    if (error) return <p style={{ color: '#ff4444', textAlign: 'center' }}>{error}</p>;
    if (!producto) return null;

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto', backgroundColor: '#111', padding: '30px', borderRadius: '15px', border: '1px solid #333' }}>
            <button onClick={alVolver} style={{ padding: '8px 15px', backgroundColor: 'transparent', color: '#00d4ff', border: '1px solid #00d4ff', borderRadius: '8px', cursor: 'pointer', marginBottom: '20px' }}>
                ⬅️ VOLVER AL CATÁLOGO
            </button>

            <div style={{ display: 'flex', gap: '30px', flexWrap: 'wrap' }}>
                {/* Lado Izquierdo: Imagen */}
                <div style={{ flex: '1 1 300px' }}>
                    <img src={producto.imagenUrl || 'https://via.placeholder.com/400x400'} alt={producto.nombre} style={{ width: '100%', borderRadius: '10px', border: '1px solid #222' }} />
                </div>

                {/* Lado Derecho: Info y Acciones */}
                <div style={{ flex: '1 1 400px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <span style={{ color: '#888', textTransform: 'uppercase', fontSize: '0.8rem' }}>{producto.categoria}</span>
                    <h2 style={{ color: '#00d4ff', margin: '10px 0', fontSize: '2rem' }}>{producto.nombre}</h2>
                    <p style={{ color: '#ccc', marginBottom: '20px' }}>{producto.descripcion || 'Sin descripción disponible.'}</p>
                    <h3 style={{ color: 'white', fontSize: '1.8rem', margin: '0 0 20px 0' }}>${producto.precio?.toLocaleString()}</h3>

                    {!esperandoPago ? (
                        <>
                            <div style={{ display: 'flex', gap: '15px', alignItems: 'center', marginBottom: '20px' }}>
                                <label style={{ color: '#888' }}>Cantidad:</label>
                                <input type="number" min="1" max={producto.stock} value={cantidad} onChange={(e) => setCantidad(Number(e.target.value))} style={{ padding: '10px', width: '70px', backgroundColor: '#050505', color: 'white', border: '1px solid #333', borderRadius: '8px' }} />
                                <span style={{ color: producto.stock < 5 ? '#ff4444' : '#44ff44', fontSize: '0.9rem' }}>Stock: {producto.stock}</span>
                            </div>

                            <div style={{ display: 'flex', gap: '15px' }}>
                                <button onClick={handleAgregarCarrito} style={{ flex: 1, padding: '15px', backgroundColor: 'transparent', color: '#00d4ff', border: '1px solid #00d4ff', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>
                                    🛒 AL CARRITO
                                </button>
                                <button onClick={handleComprarAhora} style={{ flex: 1, padding: '15px', backgroundColor: '#44ff44', color: 'black', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>
                                    💸 COMPRAR AHORA
                                </button>
                            </div>
                        </>
                    ) : (
                        <div style={{ padding: '20px', border: '1px dashed #44ff44', borderRadius: '10px', textAlign: 'center' }}>
                            <p style={{ color: '#44ff44', fontWeight: 'bold' }}>PAGO EN PROCESO EN PESTAÑA EXTERNA</p>
                            <button onClick={confirmarPagoManual} style={{ width: '100%', marginTop: '10px', padding: '12px', backgroundColor: '#44ff44', color: 'black', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' }}>
                                ✅ YA PAGUÉ, CONFIRMAR STOCK
                            </button>
                            <button onClick={() => setEsperandoPago(false)} style={{ marginTop: '10px', backgroundColor: 'transparent', color: '#888', border: 'none', cursor: 'pointer', fontSize: '0.8rem' }}>
                                Cancelar y volver
                            </button>
                        </div>
                    )}
                </div>
            </div>

            <hr style={{ borderColor: '#222', margin: '40px 0' }} />
            <div style={{ backgroundColor: '#0a0a0a', padding: '20px', borderRadius: '10px', border: '1px dashed #333' }}>
                <h3 style={{ color: '#00d4ff', textAlign: 'center' }}>⭐ Reseñas de Clientes</h3>
                <p style={{ color: '#666', textAlign: 'center', fontStyle: 'italic' }}>Componente de Esther aquí...</p>
            </div>
        </div>
    );
};

export default DetalleCatalogo;