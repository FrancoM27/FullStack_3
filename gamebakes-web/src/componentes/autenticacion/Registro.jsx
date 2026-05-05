import React, { useState } from 'react';

export default function Registro({ alVolverAlLogin }) {
    const [form, setForm] = useState({ 
        username: '', 
        email: '', 
        password: '', 
        nombreCompleto: '',
        rol: 'cliente' 
    });

    const handleRegistro = async (e) => {
        e.preventDefault();
        
        // 1. Preparamos los datos para que coincidan con el DTO de Java
        const datosParaEnviar = {
            username: form.username,
            email: form.email,
            password: form.password,
            nombreCompleto: form.nombreCompleto,
            rol: form.rol.toUpperCase() //Spring Boot espera CLIENTE o VENDEDOR
        };

        console.log("Intentando registrar en el puerto 8080:", datosParaEnviar);

        try {
            // 2. Realizamos la petición al puerto 8080 (donde está Usuarios)
            const response = await fetch('http://localhost:8080/api/usuarios/registrar', {
                method: 'POST',
                headers: { 
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify(datosParaEnviar)
            });

            // 3. Manejo de la respuesta
            if (response.ok) {
                const data = await response.json();
                console.log("Registro exitoso:", data);
                alert(`🚀 ¡Cuenta de ${form.rol.toUpperCase()} creada con éxito!`);
                alVolverAlLogin(); //Te redirige al login automáticamente
            } else {
                //Si el servidor responde con error (ej: email duplicado)
                const errorData = await response.json().catch(() => ({}));
                console.error("Respuesta de error del servidor:", errorData);
                alert(`❌ Error al registrar: ${errorData.message || "Verifica los datos o el correo"}`);
            }
        } catch (error) {
            //Si el servidor está apagado o hay error de red/CORS
            console.error("Error crítico de conexión:", error);
            alert("📡 No se pudo conectar con el servidor. Revisa que el microservicio de Usuarios (puerto 8080) esté encendido.");
        }
    };

    return (
        <div style={containerStyle}>
            <div style={cardStyle}>
                <h2 style={{ color: '#00d4ff', textAlign: 'center', marginBottom: '20px' }}>📝 NUEVA CUENTA</h2>
                <form onSubmit={handleRegistro}>
                    <input 
                        type="text" 
                        placeholder="Nombre Real" 
                        style={inputStyle} 
                        required
                        onChange={(e) => setForm({...form, nombreCompleto: e.target.value})} 
                    />
                    <input 
                        type="text" 
                        placeholder="Username" 
                        style={inputStyle} 
                        required
                        onChange={(e) => setForm({...form, username: e.target.value})} 
                    />
                    <input 
                        type="email" 
                        placeholder="Email" 
                        style={inputStyle} 
                        required
                        onChange={(e) => setForm({...form, email: e.target.value})} 
                    />
                    <input 
                        type="password" 
                        placeholder="Contraseña" 
                        style={inputStyle} 
                        required
                        onChange={(e) => setForm({...form, password: e.target.value})} 
                    />
                    
                    <label style={{ color: '#00d4ff', display: 'block', marginBottom: '10px', fontSize: '0.8rem' }}>TIPO DE PERFIL:</label>
                    <select 
                        style={inputStyle} 
                        value={form.rol} 
                        onChange={(e) => setForm({...form, rol: e.target.value})}
                    >
                        <option value="cliente">🎮 GAMER (CLIENTE)</option>
                        <option value="vendedor">🧁 MASTER BAKER (VENDEDOR)</option>
                    </select>

                    <button type="submit" style={btnStyle}>Crear Cuenta</button>
                </form>
                <button onClick={alVolverAlLogin} style={{ background: 'none', border: 'none', color: '#888', width: '100%', marginTop: '10px', cursor: 'pointer' }}>VOLVER</button>
            </div>
        </div>
    );
}

// Estilos
const containerStyle = { display: 'flex', justifyContent: 'center', alignItems: 'center', height: '100vh' };
const cardStyle = { background: 'rgba(255, 255, 255, 0.05)', padding: '40px', borderRadius: '20px', border: '1px solid rgba(255,255,255,0.1)', backdropFilter: 'blur(10px)', width: '350px' };
const inputStyle = { width: '100%', padding: '12px', marginBottom: '15px', backgroundColor: 'rgba(0,0,0,0.4)', border: '1px solid #00d4ff', borderRadius: '8px', color: 'white', boxSizing: 'border-box' };
const btnStyle = { width: '100%', padding: '12px', backgroundColor: '#00d4ff', color: 'black', border: 'none', borderRadius: '8px', fontWeight: 'bold', cursor: 'pointer', textTransform: 'uppercase' };