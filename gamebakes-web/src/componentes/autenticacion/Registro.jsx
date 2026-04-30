import React, { useState } from 'react';

export default function Registro({ alVolverAlLogin }) {
    const [form, setForm] = useState({ username: '', email: '', password: '', nombreCompleto: '' });

    const handleRegistro = async (e) => {
        e.preventDefault();
        const response = await fetch('http://localhost:8081/api/usuarios/registrar', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(form)
        });

        if (response.ok) {
            alert("¡Cuenta creada con éxito, Guerrero! Ahora inicia sesión.");
            alVolverAlLogin();
        } else {
            alert("Error al registrar. Puede que el usuario o email ya existan.");
        }
    };

    return (
        <div style={containerStyle}>
            <div style={cardStyle}>
                <h2 style={{ color: '#00d4ff', textAlign: 'center' }}>📝 NUEVA CUENTA</h2>
                <form onSubmit={handleRegistro}>
                    <input type="text" placeholder="Nombre Real" style={inputStyle} onChange={(e) => setForm({...form, nombreCompleto: e.target.value})} />
                    <input type="text" placeholder="Username" style={inputStyle} onChange={(e) => setForm({...form, username: e.target.value})} />
                    <input type="email" placeholder="Email" style={inputStyle} onChange={(e) => setForm({...form, email: e.target.value})} />
                    <input type="password" placeholder="Contraseña" style={inputStyle} onChange={(e) => setForm({...form, password: e.target.value})} />
                    <button type="submit" style={btnStyle}>Crear Cuenta</button>
                </form>
                <button onClick={alVolverAlLogin} style={{ background: 'none', border: 'none', color: '#888', width: '100%', marginTop: '10px', cursor: 'pointer' }}>Volver</button>
            </div>
        </div>
    );
}

const containerStyle = { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' };
const cardStyle = { background: 'rgba(255, 255, 255, 0.05)', padding: '40px', borderRadius: '20px', border: '1px solid rgba(255,255,255,0.1)', backdropFilter: 'blur(10px)', width: '350px' };
const inputStyle = { width: '100%', padding: '12px', marginBottom: '15px', backgroundColor: 'rgba(0,0,0,0.4)', border: '1px solid #00d4ff', borderRadius: '8px', color: 'white', boxSizing: 'border-box' };
const btnStyle = { width: '100%', padding: '12px', backgroundColor: '#00d4ff', color: 'black', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer', textTransform: 'uppercase' };