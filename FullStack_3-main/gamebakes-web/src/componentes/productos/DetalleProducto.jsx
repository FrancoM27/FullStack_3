import React from 'react';

export default function DetalleProducto({ producto, alCerrar, alEditar }) {
    const colorCian = '#00d4ff';

    if (!producto) return null;

    return (
        <div style={{ animation: 'fadeIn 0.3s' }}>
            <button onClick={alCerrar} style={btnVolverStyle}>⬅️ VOLVER AL LISTADO</button>

            <div style={detailContainer}>
                <div style={{...detailImg, backgroundImage: `url(${producto.imagenUrl || 'https://via.placeholder.com/400x300?text=Sin+Imagen'})`}}></div>

                <div style={detailInfo}>
                    <span style={tagStyle}>{producto.categoria}</span>
                    <h1 style={{color: colorCian, fontSize: '2.5rem', margin: '10px 0'}}>{producto.nombre}</h1>
                    <p style={descStyle}>{producto.descripcion}</p>

                    <div style={statsGrid}>
                        <div style={statItem}>
                            <small>PRECIO</small>
                            <p style={{fontSize: '1.5rem', fontWeight: 'bold'}}>${producto.precio.toLocaleString()}</p>
                        </div>
                        <div style={statItem}>
                            <small>STOCK</small>
                            <p style={{fontSize: '1.5rem', fontWeight: 'bold', color: colorCian}}>{producto.stock} uds.</p>
                        </div>
                    </div>

                    <button onClick={() => alEditar(producto)} style={btnEditarGrande}>✏️ EDITAR INFORMACIÓN</button>
                </div>
            </div>
        </div>
    );
}

const detailContainer = { display: 'grid', gridTemplateColumns: '1fr 1.2fr', gap: '40px', backgroundColor: '#111', padding: '40px', borderRadius: '20px', border: '1px solid #222' };
const detailImg = { height: '450px', backgroundSize: 'cover', backgroundPosition: 'center', borderRadius: '15px', border: '1px solid #333' };
const detailInfo = { display: 'flex', flexDirection: 'column', justifyContent: 'center' };
const tagStyle = { backgroundColor: 'rgba(0,212,255,0.1)', color: '#00d4ff', padding: '5px 15px', borderRadius: '20px', border: '1px solid #00d4ff', alignSelf: 'start', fontSize: '0.8rem', fontWeight: 'bold' };
const descStyle = { color: '#aaa', fontSize: '1.1rem', lineHeight: '1.6', margin: '20px 0' };
const statsGrid = { display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '20px', marginBottom: '30px', padding: '20px', backgroundColor: '#050505', borderRadius: '12px', border: '1px solid #222' };
const statItem = { color: 'white' };
const btnVolverStyle = { background: 'none', border: '1px solid #444', color: '#888', padding: '10px 20px', borderRadius: '8px', cursor: 'pointer', marginBottom: '20px' };
const btnEditarGrande = { padding: '15px', backgroundColor: '#00d4ff', color: 'black', fontWeight: 'bold', border: 'none', borderRadius: '10px', cursor: 'pointer', fontSize: '1rem' };