import React, { useState } from 'react';


export default function Login({ onLoginSuccess, alCambiarARegistro }) {
    const [datos, setDatos] = useState({ identifier: '', password: '' });
    const colorCian = '#00d4ff';

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        console.log("Intentando iniciar sesión con:", datos.identifier);

        try {
            const response = await fetch('http://localhost:8080/api/usuarios/login', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                //Aquí enviamos el objeto con 'identifier' y 'password'
                body: JSON.stringify(datos)
            });

            if (response.ok) {
                const token = await response.text();
                console.log("¡Login exitoso! Token guardado.");
                
                sessionStorage.setItem('token', token);
                
                alert("¡Bienvenido al sistema!");
                onLoginSuccess(); 
            } else {
                alert("Usuario o contraseña incorrectos");
            }
        } catch (error) {
            console.error("Error en login:", error);
            alert("No se pudo conectar con el servidor.");
        }
    };

    return (
        <div style={containerStyle}>
            <div style={cardStyle}>
                <h2 style={{ color: colorCian, textAlign: 'center', marginBottom: '20px' }}>⚡ ACCESO GAMER</h2>
                <form onSubmit={handleSubmit}>
                    <input
                        type="text"
                        placeholder="Usuario o Email"
                        style={inputStyle}
                        required
                        onChange={(e) => setDatos({...datos, identifier: e.target.value})}
                    />
                    <input
                        type="password"
                        placeholder="Contraseña"
                        style={inputStyle}
                        required
                        onChange={(e) => setDatos({...datos, password: e.target.value})}
                    />
                    <button type="submit" style={btnStyle}>Iniciar Sesión</button>
                </form>
                <p style={{ marginTop: '20px', fontSize: '0.9rem', textAlign: 'center' }}>
                    ¿No tienes cuenta?
                    <span onClick={alCambiarARegistro} style={{ color: colorCian, cursor: 'pointer', marginLeft: '5px' }}>Regístrate aquí</span>
                </p>
            </div>
        </div>
    );
}

const containerStyle = { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' };
const cardStyle = { background: 'rgba(255, 255, 255, 0.05)', padding: '40px', borderRadius: '20px', border: '1px solid rgba(255,255,255,0.1)', backdropFilter: 'blur(10px)', width: '350px' };
const inputStyle = { width: '100%', padding: '12px', marginBottom: '15px', backgroundColor: 'rgba(0,0,0,0.4)', border: '1px solid #00d4ff', borderRadius: '8px', color: 'white', boxSizing: 'border-box' };
const btnStyle = { width: '100%', padding: '12px', backgroundColor: '#00d4ff', color: 'black', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer', textTransform: 'uppercase' };