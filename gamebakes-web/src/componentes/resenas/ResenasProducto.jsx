import React, { useState } from 'react';

export default function ResenasProducto({ rol, usuarioId }) {
  const [resenas, setResenas] = useState([]);
  const [comentario, setComentario] = useState("");
  const [estrellas, setEstrellas] = useState(5);
  const [respuestaVendedor, setRespuestaVendedor] = useState({}); 
  const [cargando, setCargando] = useState(true);

  const colorCian = '#00d4ff';
  const colorMorado = '#9b59b6';
  const colorTema = rol === 'vendedor' ? colorMorado : colorCian;

  const cargarResenas = async () => {
    setCargando(true);
    try {
      // Usamos el puerto 8081 que confirmaste
      const url = rol === 'vendedor' 
        ? `http://localhost:8081/api/resenas/vendedor/${usuarioId}`
        : `http://localhost:8081/api/resenas/producto/1`; // ID 1 por defecto para clientes
      
      const response = await fetch(url);
      if (response.ok) {
        const data = await response.json();
        setResenas(Array.isArray(data) ? data : []);
      }
    } catch (error) {
      console.error("Error al cargar reseñas:", error);
    } finally {
      setCargando(false);
    }
  };

  useEffect(() => { 
    if (usuarioId) {
        cargarResenas();
    }
  }, [rol, usuarioId]);

  const enviarResena = async () => {
    if (!comentario.trim()) return alert("Escribe un comentario");
    
    const nueva = {
      productoId: 1, // Ejemplo
      clienteId: usuarioId,
      clienteNombre: "Gamer " + usuarioId,
      comentario: comentario,
      estrellas: estrellas,
      vendedorId: 2 // ID de ejemplo del vendedor
    };

    try {
        const response = await fetch('http://localhost:8081/api/resenas', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(nueva)
        });
        if (response.ok) {
            setComentario("");
            cargarResenas();
            alert("¡Reseña publicada!");
        }
    } catch (error) {
        alert("Error al conectar con el servidor de reseñas");
    }
  };

  const enviarRespuesta = async (resenaId) => {
    const textoRespuesta = respuestaVendedor[resenaId];
    if (!textoRespuesta) return alert("Escribe una respuesta");

    try {
        const response = await fetch(`http://localhost:8081/api/resenas/${resenaId}/responder`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(textoRespuesta) // Se envía como String plano
        });
        
        if (response.ok) {
            alert("¡Respuesta enviada!");
            cargarResenas();
        }
    } catch (error) {
        alert("Error al enviar respuesta");
    }
  };

  if (cargando) return <div style={{ color: colorTema, padding: '20px' }}>Cargando feedback...</div>;

  return (
    <div style={{ background: 'rgba(255, 255, 255, 0.03)', padding: '25px', borderRadius: '15px', border: `1px solid ${colorTema}`, color: 'white' }}>
      <h3 style={{ color: colorTema, textTransform: 'uppercase' }}>
        ⭐ {rol === 'vendedor' ? 'Gestión de Feedback' : 'Reseñas de la Comunidad'}
      </h3>
      
      {/* VISTA CLIENTE: Formulario para crear reseña */}
      {rol === 'cliente' && (
        <div style={{ marginBottom: '30px', borderBottom: '1px solid #333', paddingBottom: '20px' }}>
          <p>¿Qué te pareció este producto?</p>
          <div style={{ marginBottom: '10px' }}>
            {[1,2,3,4,5].map(n => (
              <span key={n} onClick={() => setEstrellas(n)} style={{ cursor:'pointer', color: n <= estrellas ? '#ffcc00' : '#444', fontSize: '25px' }}>★</span>
            ))}
          </div>
          <textarea 
            value={comentario} 
            onChange={(e) => setComentario(e.target.value)}
            placeholder="Escribe tu opinión gamer..."
            style={{ width: '100%', backgroundColor: '#111', color: 'white', border: `1px solid ${colorCian}`, borderRadius: '8px', padding: '10px', boxSizing: 'border-box' }}
          />
          <button onClick={enviarResena} style={{ width: '100%', marginTop: '10px', backgroundColor: colorCian, border: 'none', padding: '12px', fontWeight: 'bold', cursor: 'pointer', borderRadius: '8px', color: 'black' }}>
            PUBLICAR RESEÑA
          </button>
        </div>
      )}

      {/* LISTADO DE RESEÑAS */}
      <div style={{ marginTop: '20px' }}>
        {resenas.length > 0 ? (
          resenas.map(r => (
            <div key={r.id} style={{ backgroundColor: 'rgba(255,255,255,0.05)', padding: '15px', borderRadius: '10px', marginBottom: '15px', borderLeft: `4px solid ${colorTema}` }}>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginBottom: '10px' }}>
                <strong>{r.clienteNombre}</strong>
                <span style={{ color: '#ffcc00' }}>{'★'.repeat(r.estrellas)}</span>
              </div>
              <p style={{ fontStyle: 'italic' }}>"{r.comentario}"</p>
              
              {/* Respuesta del Vendedor */}
              {r.respuestaVendedor && (
                <div style={{ marginLeft: '20px', borderTop: '1px solid #444', paddingTop: '10px', marginTop: '10px', color: colorMorado }}>
                  <strong>Respuesta del Chef:</strong> {r.respuestaVendedor}
                </div>
              )}

              {/* Input para que el Vendedor responda si no lo ha hecho */}
              {rol === 'vendedor' && !r.respuestaVendedor && (
                <div style={{ marginTop: '15px', display: 'flex', gap: '10px' }}>
                  <input 
                    type="text" 
                    placeholder="Escribe tu respuesta oficial..."
                    onChange={(e) => setRespuestaVendedor({...respuestaVendedor, [r.id]: e.target.value})}
                    style={{ flexGrow: 1, padding: '8px', borderRadius: '5px', border: `1px solid ${colorMorado}`, backgroundColor: '#000', color: 'white' }}
                  />
                  <button 
                    onClick={() => enviarRespuesta(r.id)}
                    style={{ padding: '8px 15px', backgroundColor: colorMorado, border: 'none', color: 'white', borderRadius: '5px', cursor: 'pointer', fontWeight: 'bold' }}
                  >
                    RESPONDER
                  </button>
                </div>
              )}
            </div>
          ))
        ) : (
          <div style={{ textAlign: 'center', padding: '40px', color: '#666' }}>
            <p fontSize="2rem">💬</p>
            <p>No hay reseñas en este momento.</p>
          </div>
        )}
      </div>
    </div>
  );
}