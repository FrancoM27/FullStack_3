import React, { useState, useEffect } from 'react'
import SeguimientoPedidos from './componentes/seguimiento_pedidos/SeguimientoPedidos'
import ResenasProducto from './componentes/resenas/ResenasProducto'
import logo from './assets/logo_gamebakes.png'
import Login from './componentes/autenticacion/Login'
import Registro from './componentes/autenticacion/Registro'
import SolicitarRecuperacion from './componentes/autenticacion/SolicitarRecuperacion'
import RestablecerPassword from './componentes/autenticacion/RestablecerPassword'
import GestionProductos from './componentes/productos/GestionProductos'
import CatalogoProductos from './componentes/productos/CatalogoProductos'
import DetalleCatalogo from './componentes/productos/DetalleCatalogo'
import Carrito from './componentes/productos/Carrito'

import { getAuthData } from './componentes/autenticacion/authUtils'

function App() {
    const [usuario, setUsuario] = useState(() => {
        const auth = getAuthData();
        if (auth) {
            return { loggedIn: true, rol: auth.rol, id: auth.id };
        }
        return { loggedIn: false, rol: 'cliente', id: null };
    });

    const [vistaRecuperacion, setVistaRecuperacion] = useState(false);
    const [productoSeleccionado, setProductoSeleccionado] = useState(null);

    const [tokenRecuperacion, setTokenRecuperacion] = useState(() => {
        const params = new URLSearchParams(window.location.search);
        return params.get('token');
    });

    const [mostrarRegistro, setMostrarRegistro] = useState(() => {
        return sessionStorage.getItem('view') === 'registro';
    });

    const [seccionActiva, setSeccionActiva] = useState(() => {
        return sessionStorage.getItem('seccion') || 'inicio';
    });

    const manejarCambioSeccion = (id) => {
        setSeccionActiva(id);
        sessionStorage.setItem('seccion', id);
        setProductoSeleccionado(null);
    };

    const manejarCambioVista = (esRegistro) => {
        setMostrarRegistro(esRegistro);
        sessionStorage.setItem('view', esRegistro ? 'registro' : 'login');
        setVistaRecuperacion(false);
    };

    const cerrarSesion = () => {
        sessionStorage.clear();
        setUsuario({ loggedIn: false, rol: 'cliente', id: null });
        setSeccionActiva('inicio');
        setMostrarRegistro(false);
        setVistaRecuperacion(false);
    };

    if (!usuario.loggedIn) {
        if (tokenRecuperacion) {
            return (
                <RestablecerPassword
                    token={tokenRecuperacion}
                    alFinalizar={() => {
                        setTokenRecuperacion(null);
                        window.history.replaceState({}, document.title, "/");
                        setVistaRecuperacion(false);
                    }}
                />
            );
        }

        if (vistaRecuperacion) {
            return <SolicitarRecuperacion alVolverAlLogin={() => setVistaRecuperacion(false)} />;
        }

        return mostrarRegistro
            ? <Registro alVolverAlLogin={() => manejarCambioVista(false)} />
            : <Login
                onLoginSuccess={() => {
                    const auth = getAuthData();
                    setUsuario({ loggedIn: true, rol: auth.rol, id: auth.id });
                    sessionStorage.removeItem('view');
                }}
                alCambiarARegistro={() => manejarCambioVista(true)}
                alOlvidarPassword={() => setVistaRecuperacion(true)}
            />
    }

    const colorCian = '#00d4ff';
    const colorMorado = '#9b59b6';
    const colorTema = usuario.rol === 'vendedor' ? colorMorado : colorCian;

    const menuCliente = [
        { id: 'inicio', nombre: '🎮 Inicio' },
        { id: 'catalogo', nombre: '🍰 Catálogo' },
        { id: 'carrito', nombre: '🛒 Mi Carrito' },
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

                <div style={{ flexGrow: 1, overflowY: 'auto', marginBottom: '20px' }}>
                    <p style={{ color: colorTema, fontSize: '0.8rem', marginBottom: '20px', textAlign: 'center', letterSpacing: '1px' }}>
                        MODO: {usuario.rol.toUpperCase()}
                    </p>
                    {menuActual.map(item => (
                        <button
                            key={item.id}
                            onClick={() => manejarCambioSeccion(item.id)}
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

                <div style={{ paddingBottom: '10px' }}>
                    <button onClick={cerrarSesion} style={{ width: '100%', backgroundColor: 'transparent', color: '#ff4444', border: '1px solid #ff4444', padding: '12px', borderRadius: '8px', cursor: 'pointer', fontWeight: 'bold', textTransform: 'uppercase', fontSize: '0.8rem' }}>
                        ❌ Cerrar Gestión
                    </button>
                </div>
            </nav>

            <main style={{ flexGrow: 1, padding: '40px', marginLeft: '250px' }}>
                <header style={{ marginBottom: '30px', borderBottom: '1px solid #333', paddingBottom: '10px' }}>
                    <h1 style={{ color: colorTema, margin: 0, textTransform: 'uppercase', letterSpacing: '2px' }}>
                        {seccionActiva.replace('_', ' ')}
                    </h1>
                </header>

                {seccionActiva === 'pedidos' && <SeguimientoPedidos rol={usuario.rol} usuarioId={usuario.id} />}
                {seccionActiva === 'resenas' && <ResenasProducto rol={usuario.rol} usuarioId={usuario.id} />}
                {seccionActiva === 'productos' && <GestionProductos vendedorId={usuario.id}/>}

                {seccionActiva === 'catalogo' && !productoSeleccionado && (
                    <CatalogoProductos onVerDetalle={(id) => setProductoSeleccionado(id)} />
                )}

                {seccionActiva === 'catalogo' && productoSeleccionado && (
                    <DetalleCatalogo
                        productoId={productoSeleccionado}
                        rol={usuario.rol}
                        usuarioId={usuario.id}
                        alVolver={() => setProductoSeleccionado(null)}
                    />
                )}

                {seccionActiva === 'carrito' && (
                    <Carrito usuarioId={usuario.id} />
                )}

                {seccionActiva === 'inicio' && (
                    <div style={{ textAlign: 'center', marginTop: '50px' }}>
                        <h2 style={{ color: 'white' }}>¡Bienvenido Guerrero!</h2>
                        <p style={{ color: '#888' }}>Sesión activa: <strong>{usuario.rol}</strong>.</p>
                    </div>
                )}
            </main>
        </div>
    )
}

export default App