import React, { useState, useEffect } from 'react';

const Carrito = ({ usuarioId }) => {
    const [items, setItems] = useState([]);
    const [detallesProductos, setDetallesProductos] = useState({});
    const [cargando, setCargando] = useState(true);
    const [esperandoPago, setEsperandoPago] = useState(false);
    const [idPagoGenerado, setIdPagoGenerado] = useState(null);

    const token = sessionStorage.getItem('token');

    useEffect(() => {
        obtenerCarrito();
    }, []);

    const obtenerCarrito = async () => {
        try {
            const response = await fetch(`http://localhost:9000/api/pagos/carrito/${usuarioId}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();
            setItems(data);
            cargarNombresYFotos(data);
        } catch (err) {
            console.error(err);
        } finally {
            setCargando(false);
        }
    };

    const cargarNombresYFotos = async (itemsCarrito) => {
        const nuevosDetalles = {};
        for (const item of itemsCarrito) {
            try {
                const res = await fetch(`http://localhost:9000/api/productos/${item.productoId}`);
                if (res.ok) {
                    nuevosDetalles[item.productoId] = await res.json();
                }
            } catch (e) {
                console.error("Error cargando producto");
            }
        }
        setDetallesProductos(nuevosDetalles);
    };

    const handleProcederAlPago = async () => {
        try {
            const response = await fetch(`http://localhost:9000/api/pagos/iniciar-desde-carrito/${usuarioId}`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                const data = await response.json();
                if (data.transaccionId) {
                    setIdPagoGenerado(data.idPago);
                    setEsperandoPago(true);
                    window.open(data.transaccionId, '_blank');
                }
            }
        } catch (err) {
            alert("Error al iniciar el pago.");
        }
    };

    const confirmarPagoManual = async () => {
        try {
            const response = await fetch(`http://localhost:9000/api/pagos/confirmar/${idPagoGenerado}`, {
                method: 'POST',
                headers: { 'Authorization': `Bearer ${token}` }
            });
            if (response.ok) {
                alert("¡Pago verificado! El carrito se ha vaciado y tu orden está en camino. 🧁");
                setItems([]); // Vaciamos la vista
                setEsperandoPago(false);
                window.location.reload();
            }
        } catch (err) {
            alert("Todavía no se confirma el pago.");
        }
    };

    const total = items.reduce((acc, item) => acc + (item.precioUnitario * item.cantidad), 0);

    if (cargando) return <p style={{ textAlign: 'center', color: '#00d4ff' }}>Cargando carrito...</p>;

    if (esperandoPago) {
        return (
            <div style={{ textAlign: 'center', padding: '50px', backgroundColor: '#050505', borderRadius: '15px', border: '1px solid #44ff44', maxWidth: '600px', margin: '50px auto' }}>
                <h2 style={{ color: '#44ff44' }}>🚀 ¡Casi listo!</h2>
                <p>Se abrió Mercado Pago en una pestaña nueva.</p>
                <p style={{ color: '#888' }}>Cuando termines de pagar, vuelve aquí y presiona el botón para vaciar tu carrito.</p>
                <button
                    onClick={confirmarPagoManual}
                    style={{ marginTop: '30px', padding: '15px 40px', backgroundColor: '#44ff44', color: 'black', border: 'none', borderRadius: '10px', fontWeight: 'bold', cursor: 'pointer', fontSize: '1.2rem' }}
                >
                    ✅ YA PAGUÉ, LIMPIAR MI CARRITO
                </button>
            </div>
        );
    }

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto', color: 'white' }}>
            {items.length === 0 ? (
                <div style={{ textAlign: 'center', marginTop: '50px' }}>
                    <h2 style={{ color: '#666' }}>Tu carrito está vacío 🛒</h2>
                </div>
            ) : (
                <>
                    <div style={{ backgroundColor: '#111', borderRadius: '15px', overflow: 'hidden', border: '1px solid #333' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse' }}>
                            <thead>
                            <tr style={{ backgroundColor: '#050505', borderBottom: '1px solid #333', textAlign: 'left' }}>
                                <th style={{ padding: '20px' }}>Producto</th>
                                <th style={{ padding: '20px' }}>Cantidad</th>
                                <th style={{ padding: '20px' }}>Precio</th>
                                <th style={{ padding: '20px' }}>Subtotal</th>
                            </tr>
                            </thead>
                            <tbody>
                            {items.map(item => {
                                const p = detallesProductos[item.productoId] || {};
                                return (
                                    <tr key={item.id} style={{ borderBottom: '1px solid #222' }}>
                                        <td style={{ padding: '15px', display: 'flex', alignItems: 'center', gap: '15px' }}>
                                            <img src={p.imagenUrl || 'https://via.placeholder.com/50'} style={{ width: '50px', height: '50px', borderRadius: '8px', objectFit: 'cover' }} alt="thumb" />
                                            <span>{p.nombre || `Producto #${item.productoId}`}</span>
                                        </td>
                                        <td style={{ padding: '15px' }}>{item.cantidad}</td>
                                        <td style={{ padding: '15px' }}>${item.precioUnitario.toLocaleString()}</td>
                                        <td style={{ padding: '15px', color: '#00d4ff', fontWeight: 'bold' }}>
                                            ${(item.precioUnitario * item.cantidad).toLocaleString()}
                                        </td>
                                    </tr>
                                );
                            })}
                            </tbody>
                        </table>
                    </div>

                    <div style={{ marginTop: '30px', textAlign: 'right', padding: '30px', backgroundColor: '#050505', borderRadius: '15px', border: '1px solid #333' }}>
                        <h2 style={{ fontSize: '2.5rem', margin: '10px 0', color: '#00d4ff' }}>${total.toLocaleString()}</h2>
                        <button onClick={handleProcederAlPago} style={{ padding: '15px 50px', backgroundColor: '#44ff44', color: 'black', border: 'none', borderRadius: '10px', fontWeight: 'bold', cursor: 'pointer', fontSize: '1.2rem' }}>
                            💸 Proceder al Pago
                        </button>
                    </div>
                </>
            )}
        </div>
    );
};

export default Carrito;