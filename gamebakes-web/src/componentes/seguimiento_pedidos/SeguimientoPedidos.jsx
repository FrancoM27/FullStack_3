import React, { useState, useEffect } from 'react';

export default function SeguimientoPedidos({ rol }) {
  const [pedidos, setPedidos] = useState([]);
  const colorCian = '#00d4ff';

  // 1. Cargar pedidos desde el Backend (Puerto 8082)
  const cargarPedidos = async () => {
    try {
      const res = await fetch('http://localhost:8082/api/pedidos');
      const data = await res.json();
      setPedidos(data);
    } catch (err) {
      console.error("Error cargando pedidos:", err);
    }
  };

  useEffect(() => { cargarPedidos(); }, []);

  // 2. Función para que el VENDEDOR cambie el estado (Esto dispara Kafka en el Backend)
  const cambiarEstado = async (id, nuevoEstado) => {
    try {
      await fetch(`http://localhost:8082/api/pedidos/${id}/estado?nuevoEstado=${nuevoEstado}`, {
        method: 'PUT'
      });
      cargarPedidos(); // Refrescar lista
    } catch (err) {
      console.error("Error al actualizar:", err);
    }
  };

  return (
    <div style={{ padding: '20px', color: 'white', background: 'rgba(0,0,0,0.5)', borderRadius: '15px' }}>
      <h2 style={{ color: colorCian }}>📡 SEGUIMIENTO DE PEDIDOS (RF5)</h2>
      <p>Vista actual: <strong>{rol.toUpperCase()}</strong></p>

      <table style={{ width: '100%', borderCollapse: 'collapse', marginTop: '20px' }}>
        <thead>
          <tr style={{ borderBottom: `2px solid ${colorCian}` }}>
            <th>ID</th>
            <th>Producto</th>
            <th>Estado Actual</th>
            {rol === 'vendedor' && <th>Acciones</th>}
          </tr>
        </thead>
        <tbody>
          {pedidos.map(p => (
            <tr key={p.id} style={{ borderBottom: '1px solid #333', textAlign: 'center' }}>
              <td>#{p.id}</td>
              <td>{p.productoNombre}</td>
              <td>
                <span style={{ 
                  padding: '5px 10px', 
                  borderRadius: '15px', 
                  backgroundColor: p.estado === 'ENVIADO' ? '#28a745' : '#f39c12' 
                }}>
                  {p.estado}
                </span>
              </td>
              {rol === 'vendedor' && (
                <td>
                  <button onClick={() => cambiarEstado(p.id, 'PREPARANDO')} style={btnStyle}>Cocinando</button>
                  <button onClick={() => cambiarEstado(p.id, 'ENVIADO')} style={btnStyle}>Enviar</button>
                </td>
              )}
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
}

const btnStyle = {
  margin: '2px',
  cursor: 'pointer',
  backgroundColor: '#333',
  color: '#00d4ff',
  border: '1px solid #00d4ff',
  borderRadius: '5px',
  padding: '5px'
};