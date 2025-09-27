import { createContext, useEffect, useState } from "react";
import { ENDPOINTS, SERVER_URL } from "../config/constants";

export const AuthContext = createContext();

export function AuthProvider({ children }) {
    const [user, setUser] = useState('');
    const [accessToken, setAccessToken] = useState('');
    const [isAuthenticated, setIsAuthenticated] = useState(false);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        const tempIsAuth = localStorage.getItem("IS_AUTHENTICATED");
        setIsAuthenticated(tempIsAuth);
        if (tempIsAuth === "true") {
            refreshAccessToken().finally(() => setIsLoading(false));
        } else {
            setIsLoading(false);
        }
    }, []);

    async function login(email, password) {
        try {
            const LOGIN_URL = SERVER_URL + ENDPOINTS.LOGIN;

            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    email,
                    password,
                }),
                credentials: 'include',
            };

            const response = await fetch(LOGIN_URL, options);

            const data = await response.json();
            if (response.ok) {
                const { token, ...userData } = data;
                setUser(userData);
                setAccessToken(data.token);
                setIsAuthenticated(true);
                localStorage.setItem("IS_AUTHENTICATED", "true");
            } else {
                throw new Error(data.message);
            }

        } catch (error) {
            console.error("Login error: ", error);
            throw error;
        }
    }

    async function register(email, firstName, lastName, password) {
        try {
            const REGISTER_URL = SERVER_URL + ENDPOINTS.REGISTER;

            const options = {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    firstName,
                    lastName,
                    email,
                    password,
                }),
                credentials: 'include',
            };

            const response = await fetch(REGISTER_URL, options);

            const data = await response.json();
            if (response.ok) {
                const { token, ...userData } = data;
                setUser(userData);
                setAccessToken(token);
                setIsAuthenticated(true);
                localStorage.setItem("IS_AUTHENTICATED", "true");
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error("Register error: ", error);
            throw error;
        }
    }

    async function logout() {
        try {
            const LOGOUT_URL = SERVER_URL + ENDPOINTS.LOGOUT;

            const options = {
                method: 'POST',
                credentials: 'include',
            }

            const response = await fetch(LOGOUT_URL, options);

            if (response.ok) {
                setUser('');
                setAccessToken('');
                setIsAuthenticated(false);
                localStorage.setItem("IS_AUTHENTICATED", "false");
            } else {
                throw new Error(data.message);
            }
        } catch (error) {
            console.error("Logout error: ", error);
            throw error;
        }
    }

    async function refreshAccessToken() {
        try {
            const REFRESH_TOKEN_URL = SERVER_URL + ENDPOINTS.REFRESH_TOKEN;
            const options = {
                method: 'POST',
                credentials: 'include',
            }

            const response = await fetch(REFRESH_TOKEN_URL, options);

            if (response.ok) {
                const data = await response.json();
                setUser(data.user);
                setAccessToken(data.accessToken);
                setIsAuthenticated(true);
                localStorage.setItem("IS_AUTHENTICATED", "true");
                return data.accessToken;
            } else {
                setUser('');
                setAccessToken('');
                localStorage.setItem("IS_AUTHENTICATED", "false");
                return null;
            }
        } catch (error) {
            console.error("Refresh token error: ", error);
            setUser('');
            setAccessToken('');
            return null;
        }
    }

    return (
        <AuthContext.Provider value={{ user, accessToken, login, register, logout, isLoading, refreshAccessToken }}>
            {children}
        </AuthContext.Provider>
    );
}