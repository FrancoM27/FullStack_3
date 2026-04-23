import React, { useState } from 'react';

export default function ResenasProducto({ rol }) {
  const [comentario, setComentario] = useState("");
  const [estrellas, setEstrellas] = useState(5); // Estado para las estrellas seleccionadas
  const colorCian = '#00d4ff';

  const enviarResena = async () => {
    if (!comentario.trim()) return alert("Escribe un comentario primero");

    const nuevaResena = {
      productoId: 1,
      clienteNombre: "GamerInvitado",
      comentario: comentario,
      estrellas: estrellas, // Ahora envía el valor dinámico
      respuestaVendedor: ""
    };

    try {
      const response = await fetch('http://localhost:8081/api/resenas', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(nuevaResena)
      });
      if (response.ok) {
        alert(`¡Reseña de ${estrellas} estrellas guardada!`);
        setComentario("");
        setEstrellas(5); // Resetear
      }
    } catch (error) {
      console.error("Error al conectar:", error);
    }
  };

  return (
    <div style={{ 
      background: 'rgba(255, 255, 255, 0.03)', 
      padding: '25px', 
      borderRadius: '15px', 
      border: `1px solid rgba(255,255,255,0.1)`, 
      backdropFilter: 'blur(5px)' 
    }}>
      <h3 style={{ color: colorCian }}>⭐ COMUNIDAD GAMEBAKES</h3>
      
      {rol === 'cliente' && (
        <div>
          <p style={{ color: 'white', marginBottom: '10px' }}>¿Cuántos puntos de experiencia (estrellas) le das al producto?</p>
          
          {/* Selector de Estrellas */}
          <div style={{ marginBottom: '20px', fontSize: '24px' }}>
            {[1, 2, 3, 4, 5].map((num) => (
              <span 
                key={num} 
                onClick={() => setEstrellas(num)}
                style={{ 
                  cursor: 'pointer', 
                  color: num <= estrellas ? '#ffcc00' : '#444',
                  marginRight: '5px',
                  transition: '0.2s'
                }}
              >
                ★
              </span>
            ))}
            <span style={{ fontSize: '14px', color: '#888', marginLeft: '10px' }}>
              ({estrellas} / 5)
            </span>
          </div>

          <textarea 
            value={comentario}
            onChange={(e) => setComentario(e.target.value)}
            placeholder="Escribe tu opinión gamer aquí..." 
            style={{ 
              width: '100%', 
              backgroundColor: 'rgba(0,0,0,0.3)', 
              color: 'white', 
              border: `1px solid ${colorCian}`, 
              padding: '12px', 
              borderRadius: '8px',
              minHeight: '100px',
              fontFamily: 'inherit'
            }}
          />
          
          <button 
            onClick={enviarResena}
            style={{ 
              marginTop: '15px', 
              backgroundColor: colorCian, 
              color: 'black', 
              border: 'none', 
              padding: '12px 25px', 
              borderRadius: '8px', 
              fontWeight: '900', 
              cursor: 'pointer', 
              width: '100%',
              textTransform: 'uppercase'
            }}>
            Publicar Reseña
          </button>
        </div>
      )}
    </div>
  );
}