import React, { useState, useEffect } from 'react';
import ResenasProducto from '../resenas/ResenasProducto';

const DetalleCatalogo = ({ productoId, alVolver, rol }) => {
    const [producto, setProducto] = useState(null);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState('');
    const [cantidad, setCantidad] = useState(1);

    useEffect(() => {
        obtenerDetalle();
    }, [productoId]);

    const obtenerDetalle = async () => {
        try {
            setCargando(true);
            const response = await fetch(`http://localhost:9000/api/productos/${productoId}`);
            if (!response.ok) throw new Error('No se pudo cargar el producto');
            const data = await response.json();
            setProducto(data);
        } catch (err) {
            console.error(err);
            setError('Error de conexión al cargar el detalle.');
        } finally {
            setCargando(false);
        }
    };

    const handleAgregarCarrito = () => {
        console.log(`Agregando ${cantidad} unidad(es) del producto ${productoId} al carrito...`);
        alert(`¡Agregaste ${cantidad} ${producto.nombre} al carrito! 🛒`);
    };

    if (cargando) return <p style={{ color: '#00d4ff', textAlign: 'center' }}>Cargando detalles...</p>;
    if (error) return <p style={{ color: '#ff4444', textAlign: 'center' }}>{error}</p>;
    if (!producto) return null;

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto', backgroundColor: '#111', padding: '30px', borderRadius: '15px', border: '1px solid #333' }}>
            <button
                onClick={alVolver}
                style={{ padding: '8px 15px', backgroundColor: 'transparent', color: '#00d4ff', border: '1px solid #00d4ff', borderRadius: '8px', cursor: 'pointer', marginBottom: '20px' }}
            >
                ⬅️ VOLVER AL CATÁLOGO
            </button>

            <div style={{ display: 'flex', gap: '30px', flexWrap: 'wrap', marginBottom: '40px' }}>
                <div style={{ flex: '1 1 300px' }}>
                    <img
                        src={producto.imagenUrl || 'https://via.placeholder.com/400x400'}
                        alt={producto.nombre}
                        style={{ width: '100%', borderRadius: '10px', objectFit: 'cover', border: '1px solid #222' }}
                    />
                </div>

                <div style={{ flex: '1 1 400px', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <span style={{ color: '#888', textTransform: 'uppercase', fontSize: '0.8rem', letterSpacing: '1px' }}>{producto.categoria}</span>
                    <h2 style={{ color: '#00d4ff', margin: '10px 0', fontSize: '2rem' }}>{producto.nombre}</h2>
                    <p style={{ color: '#ccc', lineHeight: '1.6', marginBottom: '20px' }}>
                        {producto.descripcion || 'Sin descripción detallada disponible.'}
                    </p>

                    <h3 style={{ color: 'white', fontSize: '1.8rem', margin: '0 0 20px 0' }}>
                        ${producto.precio?.toLocaleString()}
                    </h3>

                    <div style={{ display: 'flex', gap: '15px', alignItems: 'center', marginBottom: '20px' }}>
                        <label style={{ color: '#888' }}>Cantidad:</label>
                        <input
                            type="number"
                            min="1"
                            max={producto.stock}
                            value={cantidad}
                            onChange={(e) => setCantidad(Number(e.target.value))}
                            style={{ padding: '10px', width: '70px', backgroundColor: '#050505', color: 'white', border: '1px solid #333', borderRadius: '8px', textAlign: 'center' }}
                        />
                        <span style={{ color: producto.stock < 5 ? '#ff4444' : '#44ff44', fontSize: '0.9rem' }}>
                            (Stock disponible: {producto.stock})
                        </span>
                    </div>

                    <button
                        onClick={handleAgregarCarrito}
                        disabled={producto.stock === 0}
                        style={{
                            padding: '15px',
                            backgroundColor: producto.stock === 0 ? '#444' : '#00d4ff',
                            color: producto.stock === 0 ? '#888' : 'black',
                            border: 'none',
                            borderRadius: '8px',
                            fontWeight: 'bold',
                            fontSize: '1.1rem',
                            cursor: producto.stock === 0 ? 'not-allowed' : 'pointer',
                            textTransform: 'uppercase',
                            transition: '0.3s'
                        }}
                    >
                        {producto.stock === 0 ? 'AGOTADO' : '🛒 AGREGAR AL CARRITO'}
                    </button>
                </div>
            </div>

            <hr style={{ borderColor: '#222', margin: '40px 0' }} />
            <div style={{ backgroundColor: '#0a0a0a', padding: '20px', borderRadius: '10px', border: '1px dashed #444' }}>
                <h3 style={{ color: '#00d4ff', textAlign: 'center', marginBottom: '20px' }}>⭐ Reseñas de Clientes</h3>
                {/* <ResenasProducto rol={rol} productoId={productoId} /> */}
            </div>
        </div>
    );
};

export default DetalleCatalogo;