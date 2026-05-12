import React, { useState, useEffect } from 'react';

export default function SeguimientoPedidos({ rol, usuarioId }) {
    const [pedidos, setPedidos] = useState([]);
    const colorCian = '#00d4ff';
    const colorMorado = '#9b59b6';
    const colorTema = rol === 'vendedor' ? colorMorado : colorCian;

    const cargarPedidos = async () => {
        try {
            const token = sessionStorage.getItem('token');
            const url = rol === 'vendedor'
                ? `http://localhost:8082/api/pedidos/vendedor/${usuarioId}`
                : `http://localhost:8082/api/pedidos/cliente/${usuarioId}`;

            const res = await fetch(url, {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Accept': 'application/json'
                }
            });

            if (res.ok) {
                const data = await res.json();
                setPedidos(data);
            }
        } catch (err) {
            console.error(err);
        }
    };

    useEffect(() => {
        if (usuarioId) cargarPedidos();
    }, [rol, usuarioId]);

    const cambiarEstado = async (id, nuevoEstado) => {
        try {
            const token = sessionStorage.getItem('token');
            await fetch(`http://localhost:8082/api/pedidos/${id}/estado?nuevoEstado=${nuevoEstado}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${token}`
                }
            });
            cargarPedidos();
        } catch (err) {
            console.error(err);
        }
    };

    return (
        <div style={{ padding: '20px', color: 'white', background: 'rgba(0,0,0,0.5)', borderRadius: '15px', border: `1px solid ${colorTema}` }}>
            <h2 style={{ color: colorTema }}>📡 SEGUIMIENTO DE OPERACIONES</h2>
            <p>Modo de visualización: <strong>{rol.toUpperCase()}</strong></p>

            <div style={{ overflowX: 'auto' }}>
                <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
                    <thead>
                    <tr style={{ borderBottom: `2px solid ${colorTema}`, color: colorTema }}>
                        <th style={{ padding: '10px' }}>ID</th>
                        <th style={{ padding: '10px' }}>Producto</th>
                        <th style={{ padding: '10px' }}>Estado</th>
                        {rol === 'vendedor' && <th style={{ padding: '10px' }}>Gestión</th>}
                    </tr>
                    </thead>
                    <tbody>
                    {pedidos.length === 0 ? (
                        <tr><td colSpan="4" style={{ padding: '20px', textAlign: 'center' }}>No hay registros disponibles.</td></tr>
                    ) : (
                        pedidos.map(p => (
                            <tr key={p.id} style={{ borderBottom: '1px solid #333', textAlign: 'center' }}>
                                <td style={{ padding: '15px' }}>#{p.id}</td>
                                <td style={{ padding: '15px' }}>{p.productoNombre}</td>
                                <td style={{ padding: '15px' }}>
                    <span style={{
                        padding: '5px 12px',
                        borderRadius: '15px',
                        fontSize: '0.8rem',
                        fontWeight: 'bold',
                        backgroundColor: p.estado === 'ENTREGADO' ? '#28a745' :
                            p.estado === 'EN_CAMINO' ? '#2980b9' : '#f39c12',
                        color: 'white'
                    }}>
                      {p.estado}
                    </span>
                                </td>
                                {rol === 'vendedor' && (
                                    <td style={{ padding: '15px' }}>
                                        <button onClick={() => cambiarEstado(p.id, 'PREPARACION')} style={btnAccion}>👨‍🍳 Preparar</button>
                                        <button onClick={() => cambiarEstado(p.id, 'EN_CAMINO')} style={btnAccion}>🚚 Enviar</button>
                                        <button onClick={() => cambiarEstado(p.id, 'ENTREGADO')} style={btnAccion}>✅ Entregar</button>
                                    </td>
                                )}
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>
        </div>
    );
}

const btnAccion = {
    margin: '2px',
    cursor: 'pointer',
    backgroundColor: '#1a1a1a',
    color: '#9b59b6',
    border: '1px solid #9b59b6',
    borderRadius: '5px',
    padding: '6px 10px',
    fontSize: '0.75rem',
    transition: '0.3s'
};