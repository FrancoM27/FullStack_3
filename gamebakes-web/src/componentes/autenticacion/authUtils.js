export const getAuthData = () => {
    try {
        const token = sessionStorage.getItem('token');
        if (!token) {
            console.log("❌ getAuthData: No hay token en sessionStorage");
            return null;
        }

        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const payload = JSON.parse(jsonPayload);
        console.log("✅ getAuthData - Payload decodificado:", payload);

        return {
            id: payload.id || null,
            // Importante: Asegúrate que tu backend mande el campo "rol"
            rol: payload.rol ? payload.rol.toLowerCase() : 'cliente',
            nombre: payload.sub
        };
    } catch (e) {
        console.error("🔥 getAuthData - Error crítico decodificando:", e);
        return null;
    }
};