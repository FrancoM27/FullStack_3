import React, { useState, useEffect } from 'react';

export default function ProductosArchivados({ vendedorId, alRestaurarExitoso }) {
    const [productosAnulados, setProductosAnulados] = useState([]);
    const [cargando, setCargando] = useState(true);

    const token = sessionStorage.getItem('token');
    const colorCian = '#00d4ff';

    useEffect(() => {
        if (vendedorId) {
            obtenerArchivados();
        }
    }, [vendedorId]);

    const obtenerArchivados = async () => {
        setCargando(true);
        try {
            const response = await fetch(`http://localhost:9000/api/productos/vendedor/${vendedorId}`, {
                headers: { 'Authorization': `Bearer ${token}` }
            });
            const data = await response.json();

            if (Array.isArray(data)) {
                const ocultos = data.filter(p => !p.activo);
                setProductosAnulados(ocultos);
            }
        } catch (err) {
            console.error("Error cargando archivados:", err);
        } finally {
            setCargando(false);
        }
    };

    const handleRestaurar = async (productoId) => {
        try {
            const response = await fetch(`http://localhost:9000/api/productos/${productoId}/estado?activo=true`, {
                method: 'PATCH',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'X-User-Role': 'VENDEDOR'
                }
            });

            if (response.ok) {
                alert("✅ ¡Producto restaurado con éxito! Volverá a aparecer en el catálogo público.");
                obtenerArchivados();
                if (alRestaurarExitoso) alRestaurarExitoso();
            } else {
                alert("⚠️ No se pudo restaurar el producto.");
            }
        } catch (err) {
            alert("❌ Error de conexión al intentar restaurar.");
        }
    };

    if (cargando) return <p style={{ textAlign: 'center', color: colorCian }}>Buscando en el baúl de archivados...</p>;

    return (
        <div style={gridStyle}>
            {productosAnulados.length === 0 ? (
                <p style={{ gridColumn: '1/-1', textAlign: 'center', color: '#666', padding: '40px' }}>
                    No tienes productos archivados en este momento.
                </p>
            ) : (
                productosAnulados.map(p => (
                    <div key={p.id} style={cardStyle}>
                        <div style={{ ...imgContainer, backgroundImage: `url(${p.imagenUrl || 'https://via.placeholder.com/300x200'})` }}>
                            <span style={tagStyleArchivado}>ARCHIVADO</span>
                        </div>
                        <div style={contentStyle}>
                            <h3 style={{ color: '#aaa', margin: '0 0 10px 0' }}>{p.nombre}</h3>
                            <p style={{ color: '#555', fontSize: '0.8rem', margin: '0 0 15px 0' }}>{p.categoria}</p>
                            <div style={btnGroupGrid}>
                                <button onClick={() => handleRestaurar(p.id)} style={btnRestaurar}>
                                    🔄 REHABILITAR PRODUCTO
                                </button>
                            </div>
                        </div>
                    </div>
                ))
            )}
        </div>
    );
}

const gridStyle = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '25px' };
const cardStyle = { backgroundColor: '#111', borderRadius: '15px', overflow: 'hidden', border: '1px solid #222', opacity: 0.8 };
const imgContainer = { height: '160px', backgroundSize: 'cover', backgroundPosition: 'center', position: 'relative', filter: 'grayscale(100%)' };
const tagStyleArchivado = { position: 'absolute', top: '10px', right: '10px', backgroundColor: '#ff4444', color: 'white', padding: '4px 10px', borderRadius: '10px', fontSize: '0.7rem', fontWeight: 'bold' };
const contentStyle = { padding: '20px' };
const btnGroupGrid = { display: 'flex', gap: '8px' };
const btnRestaurar = { flex: 1, padding: '10px', backgroundColor: 'transparent', border: '1px solid #44ff44', color: '#44ff44', borderRadius: '8px', cursor: 'pointer', fontSize: '0.8rem', fontWeight: 'bold' };