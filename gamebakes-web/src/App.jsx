import React, { useState, useEffect } from 'react'
import SeguimientoPedidos from './componentes/seguimiento_pedidos/SeguimientoPedidos'
import ResenasProducto from './componentes/resenas/ResenasProducto'
import logo from './assets/logo_gamebakes.png'
import Login from './componentes/autenticacion/Login'
import Registro from './componentes/autenticacion/Registro'
import { getAuthData } from './componentes/autenticacion/authUtils'

function App() {
  //El estado inicial incluye el ID del usuario para las consultas al backend
  const [usuario, setUsuario] = useState({ loggedIn: false, rol: 'cliente', id: null });
  const [mostrarRegistro, setMostrarRegistro] = useState(false);
  const [seccionActiva, setSeccionActiva] = useState('inicio');

  //Logica BFF: Al cargar la app, verificamos la identidad real del usuario
  useEffect(() => {
    const auth = getAuthData();
    if (auth) {
      setUsuario({ 
        loggedIn: true, 
        rol: auth.rol,
        id: auth.id 
      });
    }
  }, []);

  // Función para cerrar sesión corregida
  const cerrarSesion = () => {
    
    sessionStorage.removeItem('token'); 
    setUsuario({ loggedIn: false, rol: 'cliente', id: null });
  };

  if (!usuario.loggedIn) {
    return mostrarRegistro
      ? <Registro alVolverAlLogin={() => setMostrarRegistro(false)} />
      : <Login
          onLoginSuccess={() => {
            const auth = getAuthData();
            setUsuario({ loggedIn: true, rol: auth.rol, id: auth.id });
          }}
          alCambiarARegistro={() => setMostrarRegistro(true)}
        />
  }

  const colorCian = '#00d4ff';
  const colorMorado = '#9b59b6';
  const colorTema = usuario.rol === 'vendedor' ? colorMorado : colorCian;

  const menuCliente = [
    { id: 'inicio', nombre: '🎮 Inicio' },
    { id: 'catalogo', nombre: '🍰 Catálogo' },
    { id: 'pedidos', nombre: '📦 Mis Pedidos' },
    { id: 'resenas', nombre: '⭐ Escribir Reseña' }
  ];

  const menuVendedor = [
    { id: 'inicio', nombre: '🏠 Dashboard' },
    { id: 'productos', nombre: '🧁 Mis Productos' },
    { id: 'pedidos_gestion', nombre: '📋 Gestionar Ventas' },
    { id: 'resenas_gestion', nombre: '💬 Feedback Clientes' }
  ];

  const menuActual = usuario.rol === 'vendedor' ? menuVendedor : menuCliente;

  return (
    <div style={{ display: 'flex', minHeight: '100vh', backgroundColor: '#0a0a0a', color: 'white' }}>
      
      {/* SIDEBAR CORREGIDA */}
      <nav style={{ 
        width: '250px', 
        backgroundColor: 'rgba(0,0,0,0.9)', 
        borderRight: `2px solid ${colorTema}`,
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        backdropFilter: 'blur(10px)',
        position: 'fixed',
        height: '100vh',
        boxSizing: 'border-box',
        zIndex: 1000
      }}>
        <img src={logo} alt="Logo" style={{ width: '100%', marginBottom: '30px' }} />
        
        {/* Contenedor de botones con scroll si hay muchos */}
        <div style={{ flexGrow: 1, overflowY: 'auto', marginBottom: '20px' }}>
          <p style={{ color: colorTema, fontSize: '0.8rem', marginBottom: '20px', textAlign: 'center', letterSpacing: '1px' }}>
            MODO: {usuario.rol.toUpperCase()}
          </p>
          {menuActual.map(item => (
            <button 
              key={item.id}
              onClick={() => setSeccionActiva(item.id)}
              style={{
                width: '100%',
                textAlign: 'left',
                padding: '12px',
                marginBottom: '10px',
                backgroundColor: seccionActiva === item.id ? colorTema : 'transparent',
                color: seccionActiva === item.id ? 'black' : 'white',
                border: `1px solid ${seccionActiva === item.id ? colorTema : 'rgba(255,255,255,0.1)'}`,
                borderRadius: '8px',
                cursor: 'pointer',
                fontWeight: 'bold',
                transition: '0.3s'
              }}
            >
              {item.nombre}
            </button>
          ))}
        </div>

        {/* BOTÓN CERRAR SESIÓN CORREGIDO */}
        <div style={{ paddingBottom: '10px' }}>
            <button 
                onClick={cerrarSesion}
                style={{ 
                    width: '100%',
                    backgroundColor: 'transparent', 
                    color: '#ff4444', 
                    border: '1px solid #ff4444',
                    padding: '12px',
                    borderRadius: '8px',
                    cursor: 'pointer',
                    fontWeight: 'bold',
                    textTransform: 'uppercase',
                    fontSize: '0.8rem'
                }}
                onMouseOver={(e) => e.target.style.backgroundColor = 'rgba(255, 68, 68, 0.1)'}
                onMouseOut={(e) => e.target.style.backgroundColor = 'transparent'}
            >
                ❌ Cerrar Gestión
            </button>
        </div>
      </nav>

      {/* CONTENIDO PRINCIPAL */}
      <main style={{ flexGrow: 1, padding: '40px', marginLeft: '250px' }}>
        <header style={{ marginBottom: '30px', borderBottom: '1px solid #333', paddingBottom: '10px' }}>
          <h1 style={{ color: colorTema, margin: 0, textTransform: 'uppercase', letterSpacing: '2px' }}>
            {seccionActiva.replace('_', ' ')}
          </h1>
        </header>

        {/* MÓDULO DE PEDIDOS */}
        {(seccionActiva === 'pedidos' || seccionActiva === 'pedidos_gestion') && (
          <SeguimientoPedidos rol={usuario.rol} usuarioId={usuario.id} />
        )}

        {/* MÓDULO DE RESEÑAS CON MENSAJE VACÍO */}
        {(seccionActiva === 'resenas' || seccionActiva === 'resenas_gestion') && (
          <div>
            <ResenasProducto rol={usuario.rol} usuarioId={usuario.id} />
            {/* Aquí podrías añadir lógica si ResenasProducto viene vacío */}
            <p style={{ color: '#666', textAlign: 'center', marginTop: '20px', fontStyle: 'italic' }}>
                --- Fin de la sección de feedback ---
            </p>
          </div>
        )}

        {/* VISTA DE INICIO */}
        {seccionActiva === 'inicio' && (
          <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h2 style={{ color: 'white' }}>¡Bienvenido Guerrero, de vuelta a la cocina!</h2>
            <p style={{ color: '#888' }}>Tu sesión como <strong>{usuario.rol}</strong> está activa.</p>
            <div style={{ 
              marginTop: '30px', 
              padding: '20px', 
              border: `1px dashed ${colorTema}`, 
              borderRadius: '15px',
              display: 'inline-block'
            }}>
              <p>Selecciona una opción del menú para gestionar tus {usuario.rol === 'cliente' ? 'compras' : 'ventas'}.</p>
            </div>
          </div>
        )}
      </main>
    </div>
  )
}

export default App