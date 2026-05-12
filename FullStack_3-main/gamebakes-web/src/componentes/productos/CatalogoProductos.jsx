import React, { useState, useEffect } from 'react';

const CatalogoProductos = () => {
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [error, setError] = useState(null);
    const [filtroCategoria, setFiltroCategoria] = useState('Todos');
    const [categorias, setCategorias] = useState([]);

    useEffect(() => {
        traerProductosActivos();
    }, []);

    const traerProductosActivos = async () => {
        try {
            setCargando(true);
            const response = await fetch('http://localhost:8085/api/productos');
            if (!response.ok) throw new Error('Error al traer productos');
            const data = await response.json();
            setProductos(data);

            const cats = ['Todos', ...new Set(data.map(p => p.categoria).filter(c => c))];
            setCategorias(cats);
        } catch (err) {
            console.error('Error:', err);
            setError('No se pudieron cargar los productos');
        } finally {
            setCargando(false);
        }
    };

    const productosFiltrados = filtroCategoria === 'Todos'
        ? productos
        : productos.filter(p => p.categoria === filtroCategoria);

    if (cargando) return <p style={{ color: '#888' }}>Cargando catálogo...</p>;
    if (error) return <p style={{ color: '#ff4444' }}>{error}</p>;

    return (
        <div>
            <div style={{ marginBottom: '30px' }}>
                <h2 style={{ color: '#00d4ff', marginBottom: '20px' }}>🍰 Catálogo de Productos</h2>
                
                <div style={{ display: 'flex', gap: '10px', flexWrap: 'wrap', marginBottom: '20px' }}>
                    {categorias.map(cat => (
                        <button
                            key={cat}
                            onClick={() => setFiltroCategoria(cat)}
                            style={{
                                padding: '10px 20px',
                                backgroundColor: filtroCategoria === cat ? '#00d4ff' : 'rgba(0,212,255,0.2)',
                                color: filtroCategoria === cat ? 'black' : '#00d4ff',
                                border: '1px solid #00d4ff',
                                borderRadius: '20px',
                                cursor: 'pointer',
                                fontWeight: 'bold',
                                transition: '0.3s'
                            }}
                        >
                            {cat}
                        </button>
                    ))}
                </div>
            </div>

            <div style={{
                display: 'grid',
                gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))',
                gap: '20px'
            }}>
                {productosFiltrados.map(producto => (
                    <div
                        key={producto.id}
                        style={{
                            backgroundColor: 'rgba(0,212,255,0.05)',
                            border: '1px solid rgba(0,212,255,0.3)',
                            borderRadius: '10px',
                            overflow: 'hidden',
                            transition: 'transform 0.3s, box-shadow 0.3s'
                        }}
                        onMouseEnter={(e) => {
                            e.currentTarget.style.transform = 'translateY(-5px)';
                            e.currentTarget.style.boxShadow = '0 0 20px rgba(0,212,255,0.5)';
                        }}
                        onMouseLeave={(e) => {
                            e.currentTarget.style.transform = 'translateY(0)';
                            e.currentTarget.style.boxShadow = 'none';
                        }}
                    >
                        {producto.imagenUrl && (
                            <img
                                src={producto.imagenUrl}
                                alt={producto.nombre}
                                style={{
                                    width: '100%',
                                    height: '200px',
                                    objectFit: 'cover',
                                    backgroundColor: '#1a1a1a'
                                }}
                            />
                        )}
                        <div style={{ padding: '15px' }}>
                            <h3 style={{ margin: '0 0 10px 0', color: '#00d4ff', fontSize: '1.1rem' }}>
                                {producto.nombre}
                            </h3>
                            {producto.categoria && (
                                <p style={{ margin: '5px 0', color: '#888', fontSize: '0.85rem' }}>
                                    📂 {producto.categoria}
                                </p>
                            )}
                            <p style={{ margin: '10px 0', color: '#aaa', fontSize: '0.9rem', minHeight: '40px' }}>
                                {producto.descripcion || 'Sin descripción'}
                            </p>
                            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '15px' }}>
                                <span style={{ color: '#00d4ff', fontSize: '1.2rem', fontWeight: 'bold' }}>
                                    ${producto.precio}
                                </span>
                                <span style={{ color: '#888', fontSize: '0.9rem' }}>
                                    Stock: {producto.stock}
                                </span>
                            </div>
                        </div>
                    </div>
                ))}
            </div>

            {productosFiltrados.length === 0 && (
                <div style={{ textAlign: 'center', padding: '40px', color: '#888' }}>
                    <p>No hay productos disponibles en esta categoría.</p>
                </div>
            )}
        </div>
    );
};

export default CatalogoProductos;
