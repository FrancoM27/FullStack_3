import React, {useState, useEffect} from 'react';
import ResenasProducto from '../resenas/ResenasProducto';
import {getAuthData} from '../autenticacion/authUtils';

const DetalleCatalogo = ({productoId, alVolver, rol, usuarioId}) => {
    const [producto, setProducto] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState('');
    const [cantidad, setCantidad] = useState(1);

    // Nuevo estado para la animación de carga hacia MP
    const [redireccionando, setRedireccionando] = useState(false);

    const [haComprado, setHaComprado] = useState(false);

    const token = sessionStorage.getItem('token');

    useEffect(() => {
        obtenerDetalle();
        if (token && rol === 'cliente') {
            verificarCompra();
        }
    }, [productoId]);

    const obtenerDetalle = async () => {
        try {
            setCargando(true);
            const headers = {'Authorization': `Bearer ${token}`};
            const response = await fetch(`${import.meta.env.VITE_API_URL}/bff/productos/${productoId}/detalle-completo`, {
                headers: token ? headers : {}
            });
            if (!response.ok) throw new Error('Error');
            const data = await response.json();
            setProducto(data.producto);
            if (data.haComprado !== undefined) {
                setHaComprado(data.haComprado);
            }
        } catch (err) {
            setError('Error al cargar el producto.');
        } finally {
            setCargando(false);
        }
    };

    const verificarCompra = async () => {
        // Lógica futura
    };

    const handleAgregarCarrito = async () => {
        if (!token) return alert("Por favor, inicia sesión para agregar productos al carrito.");

        const item = {
            clienteId: usuarioId,
            productoId: producto.id,
            cantidad: cantidad,
            precioUnitario: producto.precio
        };

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/pagos/carrito/agregar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(item)
            });

            if (response.ok) {
                alert("🛒 ¡Producto agregado al carrito con éxito!");
            } else {
                const errorText = await response.text();
                alert(`⚠️ Atención: ${errorText}`);
            }
        } catch (err) {
            alert("❌ Ocurrió un error de conexión con el servidor.");
        }
    };

    const handleComprarAhora = async () => {
        if (!token) return alert("Por favor, inicia sesión para comprar.");

        setRedireccionando(true); // Iniciamos la animación

        const solicitud = {
            productoId: producto.id,
            cantidad: cantidad,
            monto: producto.precio * cantidad,
            descripcion: `Compra ${producto.nombre}`,
            clienteId: usuarioId
        };

        try {
            const response = await fetch(`${import.meta.env.VITE_API_URL}/api/pagos/iniciar`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${token}`
                },
                body: JSON.stringify(solicitud)
            });

            if (response.ok) {
                const data = await response.json();
                if (data.transaccionId) {
                    // Magia: Redirigimos en la misma pestaña para que funcione el Back URL
                    window.location.href = data.transaccionId;
                }
            } else {
                const errorText = await response.text();
                alert(`⚠️ No se pudo iniciar el pago: ${errorText}`);
                setRedireccionando(false);
            }
        } catch (err) {
            alert("❌ Error de conexión al intentar procesar el pago.");
            setRedireccionando(false);
        }
    };

    if (cargando) return <p style={{color: '#00d4ff', textAlign: 'center'}}>Cargando...</p>;
    if (error) return <p style={{color: '#ff4444', textAlign: 'center'}}>{error}</p>;
    if (!producto) return null;

    return (
        <div style={{maxWidth: '900px', margin: '0 auto'}}>
            <div style={{backgroundColor: '#111', padding: '30px', borderRadius: '15px', border: '1px solid #333'}}>
                <button onClick={alVolver} style={{
                    padding: '8px 15px',
                    backgroundColor: 'transparent',
                    color: '#00d4ff',
                    border: '1px solid #00d4ff',
                    borderRadius: '8px',
                    cursor: 'pointer',
                    marginBottom: '20px'
                }}>
                    ⬅️ VOLVER
                </button>

                <div style={{display: 'flex', gap: '30px', flexWrap: 'wrap'}}>
                    <div style={{flex: '1 1 300px'}}>
                        <img src={producto.imagenUrl || 'https://via.placeholder.com/400x400'} alt={producto.nombre}
                             style={{width: '100%', borderRadius: '10px', border: '1px solid #222'}}/>
                    </div>

                    <div style={{flex: '1 1 400px', display: 'flex', flexDirection: 'column', justifyContent: 'center'}}>
                        <span style={{color: '#888', textTransform: 'uppercase', fontSize: '0.8rem'}}>{producto.categoria}</span>
                        <h2 style={{color: '#00d4ff', margin: '10px 0', fontSize: '2rem'}}>{producto.nombre}</h2>
                        <p style={{color: '#ccc', marginBottom: '20px'}}>{producto.descripcion}</p>
                        <h3 style={{color: 'white', fontSize: '1.8rem', margin: '0 0 20px 0'}}>${producto.precio?.toLocaleString()}</h3>

                        {/* Cambio Visual: Si estamos redirigiendo, mostramos loading. Si no, los botones normales */}
                        {redireccionando ? (
                            <div style={{
                                padding: '20px',
                                border: '1px solid #00d4ff',
                                borderRadius: '10px',
                                textAlign: 'center',
                                backgroundColor: 'rgba(0, 212, 255, 0.1)'
                            }}>
                                <div style={{
                                    border: '4px solid rgba(0, 212, 255, 0.3)',
                                    borderTop: '4px solid #00d4ff',
                                    borderRadius: '50%',
                                    width: '30px',
                                    height: '30px',
                                    animation: 'spin 1s linear infinite',
                                    margin: '0 auto 15px'
                                }} />
                                <p style={{color: '#00d4ff', fontWeight: 'bold'}}>🔒 Conectando con Mercado Pago...</p>
                                <p style={{color: '#888', fontSize: '0.8rem', marginTop: '5px'}}>Serás redirigido en un momento</p>
                                {/* Keyframes inyectados rápidamente para el spinner */}
                                <style>{`@keyframes spin { 0% { transform: rotate(0deg); } 100% { transform: rotate(360deg); } }`}</style>
                            </div>
                        ) : (
                            <>
                                <div style={{display: 'flex', gap: '15px', alignItems: 'center', marginBottom: '20px'}}>
                                    <label style={{color: '#888'}}>Cantidad:</label>
                                    <input type="number" min="1" max={producto.stock} value={cantidad}
                                           onChange={(e) => setCantidad(Number(e.target.value))} style={{
                                        padding: '10px',
                                        width: '70px',
                                        backgroundColor: '#050505',
                                        color: 'white',
                                        border: '1px solid #333',
                                        borderRadius: '8px'
                                    }}/>
                                    <span style={{
                                        color: producto.stock < 5 ? '#ff4444' : '#44ff44',
                                        fontSize: '0.8rem'
                                    }}>Stock: {producto.stock}</span>
                                </div>

                                <div style={{display: 'flex', gap: '15px'}}>
                                    <button onClick={handleAgregarCarrito} style={{
                                        flex: 1,
                                        padding: '15px',
                                        backgroundColor: 'transparent',
                                        color: '#00d4ff',
                                        border: '1px solid #00d4ff',
                                        borderRadius: '8px',
                                        fontWeight: 'bold',
                                        cursor: 'pointer'
                                    }}>
                                        🛒 AL CARRITO
                                    </button>
                                    <button onClick={handleComprarAhora} style={{
                                        flex: 1,
                                        padding: '15px',
                                        backgroundColor: '#44ff44',
                                        color: 'black',
                                        border: 'none',
                                        borderRadius: '8px',
                                        fontWeight: 'bold',
                                        cursor: 'pointer'
                                    }}>
                                        💸 COMPRAR AHORA
                                    </button>
                                </div>
                            </>
                        )}
                    </div>
                </div>
            </div>

            <ResenasProducto
                rol={rol}
                usuarioId={usuarioId}
                productoId={producto.id}
                productoNombre={producto.nombre}
                vendedorId={producto.vendedorId}
                haComprado={haComprado}
            />
        </div>
    );
};

export default DetalleCatalogo;