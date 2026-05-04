export const getAuthData = () => {
    const token = sessionStorage.getItem('token');
    
    if (!token) return null;

    try {
        
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        
        //Decodificación del payload para manejar caracteres especiales (acentos, etc.)
        const jsonPayload = decodeURIComponent(window.atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));

        const payload = JSON.parse(jsonPayload);
        
        //Verificamos si el token ha expirado (opcional pero recomendado)
        const ahora = Math.floor(Date.now() / 1000);
        if (payload.exp && payload.exp < ahora) {
            console.warn("El token ha expirado");
            sessionStorage.removeItem('token');
            return null;
        }

        //Retornamos los datos decodificados del JWT
        return {
            id: payload.id, 
            rol: payload.rol?.toLowerCase(),
            nombre: payload.sub //El 'sub' suele ser el username en Spring Boot
        };
    } catch (e) {
        console.error("Error al decodificar el token", e);
        sessionStorage.removeItem('token'); //Si el token es corrupto, lo limpiamos
        return null;
    }
};