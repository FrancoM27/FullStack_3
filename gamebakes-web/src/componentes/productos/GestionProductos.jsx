import React, { useState, useEffect } from 'react';
import DetalleProducto from './DetalleProducto';

const CATEGORIAS = ["Tortas", "Cupcakes", "Galletas", "Pies & Tartas", "Edición Especial"];

export default function GestionProductos({ vendedorId }) {
    const [productos, setProductos] = useState([]);
    const [cargando, setCargando] = useState(true);
    const [vista, setVista] = useState('grid');
    const [seleccionado, setSeleccionado] = useState(null);
    const [error, setError] = useState('');

    const [nuevoProd, setNuevoProd] = useState({
        nombre: '', descripcion: '', precio: '', stock: '', categoria: CATEGORIAS[0], imagenUrl: ''
    });

    const token = sessionStorage.getItem('token');
    const colorCian = '#00d4ff';

    useEffect(() => {
        if (vendedorId) {
            obtenerProductos();
        } else {
            setCargando(false);
        }
    }, [vendedorId]);

    const obtenerProductos = async () => {
        setCargando(true);
        try {
            const response = await fetch(`http://localhost:8085/api/productos/vendedor/${vendedorId}`);
            const data = await response.json();
            setProductos(data);
        } catch (err) {
            console.error("Error cargando productos:", err);
        } finally {
            setCargando(false);
        }
    };

    const handleGuardar = async (e) => {
        e.preventDefault();
        setError('');

        const p = vista === 'edit' ? seleccionado.precio : nuevoProd.precio;
        const s = vista === 'edit' ? seleccionado.stock : nuevoProd.stock;

        if (p < 0 || s < 0) {
            setError("⚠️ El precio y el stock no pueden ser negativos.");
            return;
        }

        const esEdit = vista === 'edit';
        const url = esEdit ? `http://localhost:8085/api/productos/${seleccionado.id}` : 'http://localhost:8085/api/productos';
        const metodo = esEdit ? 'PUT' : 'POST';
        const dataBody = esEdit ? seleccionado : { ...nuevoProd, vendedorId };

        try {
            const response = await fetch(url, {
                method: metodo,
                headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
                body: JSON.stringify(dataBody)
            });

            if (response.ok) {
                setVista('grid');
                setSeleccionado(null);
                setNuevoProd({ nombre: '', descripcion: '', precio: '', stock: '', categoria: CATEGORIAS[0], imagenUrl: '' });
                obtenerProductos();
            } else {
                setError("❌ Error al guardar. Revisa los datos.");
            }
        } catch (err) {
            setError("🔥 Error de conexión con el servidor.");
        }
    };

    const handleEliminar = async (id) => {
        if (!window.confirm("¿Retirar del inventario?")) return;
        await fetch(`http://localhost:8085/api/productos/${id}`, {
            method: 'DELETE',
            headers: { 'Authorization': `Bearer ${token}` }
        });
        obtenerProductos();
    };

    if (vista === 'detalle') {
        return <DetalleProducto
            producto={seleccionado}
            alCerrar={() => setVista('grid')}
            alEditar={(p) => { setSeleccionado(p); setVista('edit'); }}
        />;
    }

    if (!vendedorId && !cargando) {
        return <p style={{color: 'red', textAlign: 'center'}}>Error: No se pudo identificar al vendedor. Re-loguea.</p>;
    }

    return (
        <div style={{ maxWidth: '1200px', margin: '0 auto' }}>
            <div style={headerStyle}>
                <h2 style={{ color: colorCian, margin: 0, letterSpacing: '2px' }}>
                    {vista === 'grid' ? '📦 MI INVENTARIO' : (vista === 'edit' ? '✏️ EDITAR PRODUCTO' : '🚀 NUEVO PRODUCTO')}
                </h2>
                {vista === 'grid' ? (
                    <button onClick={() => { setVista('form'); setError(''); }} style={btnSubirStyle}>🚀 SUBIR PRODUCTO</button>
                ) : (
                    <button onClick={() => { setVista('grid'); setSeleccionado(null); }} style={btnVolverStyle}>⬅️ CANCELAR</button>
                )}
            </div>

            {cargando ? (
                <p style={{textAlign:'center', color: colorCian}}>Cargando base de datos...</p>
            ) : (
                <>
                    {vista === 'grid' && (
                        <div style={gridStyle}>
                            {productos.length === 0 ? (
                                <p style={{gridColumn: '1/-1', textAlign: 'center', color: '#666'}}>No tienes productos registrados.</p>
                            ) : (
                                productos.map(p => (
                                    <div key={p.id} style={cardStyle}>
                                        <div style={{...imgContainer, backgroundImage: `url(${p.imagenUrl || 'https://via.placeholder.com/300x200'})`}}>
                                            <span style={tagStyleGrid}>{p.categoria}</span>
                                        </div>
                                        <div style={contentStyle}>
                                            <h3 style={{color: colorCian, margin: '0 0 10px 0'}}>{p.nombre}</h3>
                                            <div style={infoRow}>
                                                <span style={{fontWeight: 'bold'}}>${p.precio.toLocaleString()}</span>
                                                <span style={{color: p.stock < 5 ? '#ff4444' : '#44ff44', fontSize: '0.8rem'}}>Stock: {p.stock}</span>
                                            </div>
                                            <div style={btnGroupGrid}>
                                                <button onClick={() => { setSeleccionado(p); setVista('detalle'); }} style={btnMini}>VER</button>
                                                <button onClick={() => { setSeleccionado(p); setVista('edit'); }} style={btnMini}>EDITAR</button>
                                                <button onClick={() => handleEliminar(p.id)} style={{...btnMini, color: '#ff4444'}}>BORRAR</button>
                                            </div>
                                        </div>
                                    </div>
                                ))
                            )}
                        </div>
                    )}

                    {(vista === 'form' || vista === 'edit') && (
                        <div style={formCardStyle}>
                            {error && (
                                <div style={{ backgroundColor: 'rgba(255, 68, 68, 0.1)', color: '#ff4444', padding: '15px', borderRadius: '8px', marginBottom: '20px', border: '1px solid #ff4444', textAlign: 'center', fontWeight: 'bold' }}>
                                    {error}
                                </div>
                            )}

                            <form onSubmit={handleGuardar} style={gridForm}>
                                <div style={fieldGroup}><label style={labelStyle}>Nombre</label>
                                    <input type="text" style={inputStyle} value={vista === 'edit' ? seleccionado.nombre : nuevoProd.nombre} onChange={e => vista === 'edit' ? setSeleccionado({...seleccionado, nombre: e.target.value}) : setNuevoProd({...nuevoProd, nombre: e.target.value})} required />
                                </div>
                                <div style={fieldGroup}><label style={labelStyle}>Categoría</label>
                                    <select style={inputStyle} value={vista === 'edit' ? seleccionado.categoria : nuevoProd.categoria} onChange={e => vista === 'edit' ? setSeleccionado({...seleccionado, categoria: e.target.value}) : setNuevoProd({...nuevoProd, categoria: e.target.value})}>
                                        {CATEGORIAS.map(cat => <option key={cat} value={cat}>{cat}</option>)}
                                    </select>
                                </div>
                                <div style={fieldGroup}><label style={labelStyle}>Precio ($)</label>
                                    <input type="number" min="0" style={inputStyle} value={vista === 'edit' ? seleccionado.precio : nuevoProd.precio} onChange={e => vista === 'edit' ? setSeleccionado({...seleccionado, precio: e.target.value}) : setNuevoProd({...nuevoProd, precio: e.target.value})} required />
                                </div>
                                <div style={fieldGroup}><label style={labelStyle}>Stock</label>
                                    <input type="number" min="0" style={inputStyle} value={vista === 'edit' ? seleccionado.stock : nuevoProd.stock} onChange={e => vista === 'edit' ? setSeleccionado({...seleccionado, stock: e.target.value}) : setNuevoProd({...nuevoProd, stock: e.target.value})} required />
                                </div>
                                <div style={{...fieldGroup, gridColumn: 'span 2'}}><label style={labelStyle}>URL Imagen</label>
                                    <input type="text" style={inputStyle} value={vista === 'edit' ? seleccionado.imagenUrl : nuevoProd.imagenUrl} onChange={e => vista === 'edit' ? setSeleccionado({...seleccionado, imagenUrl: e.target.value}) : setNuevoProd({...nuevoProd, imagenUrl: e.target.value})} />
                                </div>
                                <div style={{...fieldGroup, gridColumn: 'span 2'}}><label style={labelStyle}>Descripción</label>
                                    <textarea style={{...inputStyle, height: '100px'}} value={vista === 'edit' ? seleccionado.descripcion : nuevoProd.descripcion} onChange={e => vista === 'edit' ? setSeleccionado({...seleccionado, descripcion: e.target.value}) : setNuevoProd({...nuevoProd, descripcion: e.target.value})} />
                                </div>
                                <button type="submit" style={saveBtnStyle}>{vista === 'edit' ? 'APLICAR CAMBIOS' : 'CREAR PRODUCTO'}</button>
                            </form>
                        </div>
                    )}
                </>
            )}
        </div>
    );
}

const gridStyle = { display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(280px, 1fr))', gap: '25px' };
const cardStyle = { backgroundColor: '#111', borderRadius: '15px', overflow: 'hidden', border: '1px solid #333' };
const imgContainer = { height: '160px', backgroundSize: 'cover', backgroundPosition: 'center', position: 'relative' };
const tagStyleGrid = { position: 'absolute', top: '10px', right: '10px', backgroundColor: 'rgba(0,0,0,0.8)', color: '#00d4ff', padding: '4px 10px', borderRadius: '10px', fontSize: '0.7rem', fontWeight: 'bold', border: '1px solid #00d4ff' };
const contentStyle = { padding: '20px' };
const infoRow = { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '15px' };
const btnGroupGrid = { display: 'flex', gap: '8px' };
const btnMini = { flex: 1, padding: '8px', backgroundColor: 'transparent', border: '1px solid #333', color: 'white', borderRadius: '5px', cursor: 'pointer', fontSize: '0.7rem' };
const headerStyle = { display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '30px' };
const formCardStyle = { background: '#111', padding: '30px', borderRadius: '15px', border: '1px solid #222' };
const gridForm = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px' };
const fieldGroup = { display: 'flex', flexDirection: 'column', gap: '8px' };
const labelStyle = { fontSize: '0.75rem', color: '#555', textTransform: 'uppercase' };
const inputStyle = { padding: '12px', backgroundColor: '#050505', color: 'white', border: '1px solid #333', borderRadius: '8px', outline: 'none' };
const btnSubirStyle = { padding: '10px 20px', backgroundColor: '#00d4ff', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer' };
const btnVolverStyle = { padding: '10px 20px', backgroundColor: 'transparent', border: '1px solid #ff4444', color: '#ff4444', borderRadius: '8px', cursor: 'pointer' };
const saveBtnStyle = { gridColumn: 'span 2', padding: '15px', backgroundColor: '#00d4ff', color: 'black', fontWeight: 'bold', border: 'none', borderRadius: '8px', cursor: 'pointer' };