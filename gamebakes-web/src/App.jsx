import React, { useState, useEffect } from 'react'
import SeguimientoPedidos from './componentes/seguimiento_pedidos/SeguimientoPedidos'
import ResenasProducto from './componentes/resenas/ResenasProducto'
import logo from './assets/logo_gamebakes.png'
import Login from './componentes/autenticacion/Login'
import Registro from './componentes/autenticacion/Registro'

function App() {
  const [usuario, setUsuario] = useState({ loggedIn: false, rol: 'cliente' });
  const [mostrarRegistro, setMostrarRegistro] = useState(false);
  const [seccionActiva, setSeccionActiva] = useState('inicio');

    useEffect(() => {
        const token = localStorage.getItem('token');
        if (token) {
            // Por ahora confiamos en que el token existe,
            // más adelante podrías validar si expiró.
            setUsuario({ loggedIn: true, rol: 'cliente' });
        }
    }, []);

    if (!usuario.loggedIn) {
        return mostrarRegistro
            ? <Registro alVolverAlLogin={() => setMostrarRegistro(false)} />
            : <Login
                onLoginSuccess={() => setUsuario({ loggedIn: true, rol: 'cliente' })}
                alCambiarARegistro={() => setMostrarRegistro(true)}
            />
    }

  const colorCian = '#00d4ff';
  const colorMorado = '#9b59b6';

  // Menús dinámicos según el rol
  const menuCliente = [
    { id: 'inicio', nombre: '🎮 Inicio' },
    { id: 'catalogo', nombre: '🍰 Catálogo' },
    { id: 'pedidos', nombre: '📦 Mis Pedidos' },
    { id: 'resenas', nombre: '⭐ Reseñas' }
  ];

  const menuVendedor = [
    { id: 'inicio', nombre: '🏠 Dashboard' },
    { id: 'productos', nombre: '🧁 Mis Productos' },
    { id: 'pedidos_gestion', nombre: '📋 Gestionar Pedidos' },
    { id: 'resenas_gestion', nombre: '💬 Feedback Clientes' }
  ];

  const menuActual = usuario.rol === 'cliente' ? menuCliente : menuVendedor;
  const colorTema = usuario.rol === 'cliente' ? colorCian : colorMorado;

  return (
    <div style={{ display: 'flex', minHeight: '100vh' }}>
      
      {/* SIDEBAR (MENÚ LATERAL) */}
      <nav style={{ 
        width: '250px', 
        backgroundColor: 'rgba(0,0,0,0.8)', 
        borderRight: `1px solid ${colorTema}`,
        padding: '20px',
        display: 'flex',
        flexDirection: 'column',
        backdropFilter: 'blur(10px)'
      }}>
        <img src={logo} alt="Logo" style={{ width: '100%', marginBottom: '30px' }} />
        
        <div style={{ flexGrow: 1 }}>
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
                border: 'none',
                borderRadius: '8px',
                cursor: 'pointer',
                fontWeight: 'bold'
              }}
            >
              {item.nombre}
            </button>
          ))}
        </div>

        <button 
          onClick={() => setUsuario({ ...usuario, rol: usuario.rol === 'cliente' ? 'vendedor' : 'cliente' })}
          style={{ fontSize: '0.7rem', opacity: 0.5, background: 'none', color: 'white', border: '1px solid white' }}
        >
          DEBUG: Cambiar Rol
        </button>
      </nav>

      {/* CONTENIDO PRINCIPAL */}
      <main style={{ flexGrow: 1, padding: '40px' }}>
        <header style={{ marginBottom: '30px', borderBottom: '1px solid #333', paddingBottom: '10px' }}>
          <h1 style={{ color: colorTema, margin: 0 }}>{seccionActiva.toUpperCase()}</h1>
        </header>

        {/* Aquí mostramos tus módulos según la sección del menú */}
        {seccionActiva === 'pedidos' || seccionActiva === 'pedidos_gestion' ? (
          <SeguimientoPedidos rol={usuario.rol} />
        ) : null}

        {seccionActiva === 'resenas' || seccionActiva === 'resenas_gestion' ? (
          <ResenasProducto rol={usuario.rol} />
        ) : null}

        {seccionActiva === 'inicio' && (
          <div style={{ textAlign: 'center', marginTop: '50px' }}>
            <h2>Bienvenido a GameBakes</h2>
            <p>Selecciona una opción del menú para comenzar.</p>
          </div>
        )}
      </main>
    </div>
  )
}

export default App