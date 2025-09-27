import { useContext } from "react";
import { SERVER_URL } from "../config/constants";
import { useAuth } from "./useAuth";
import { AppContext } from "../contexts/AppContext";

export function useApi() {
    const { accessToken, refreshAccessToken, logout } = useAuth();
    const context = useContext(AppContext);

    async function get(endpoint) {
        let token = accessToken;

        if (!token) {
            token = await refreshAccessToken();
            if (!token) {
                await logout();
                throw new Error("Session expired!");
            }
        }

        let response = await fetch(`${SERVER_URL}${endpoint}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
            },
            credentials: 'include',
        });

        if (response.status === 401) {
            const newAccessToken = await refreshAccessToken();
            if (!newAccessToken) {
                await logout();
                throw new Error("Session expired!");
            }
            response = await fetch(`${SERVER_URL}${endpoint}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${newAccessToken}`,
                },
                credentials: 'include',
            });
        }
        return response.json();
    }

    async function post(endpoint, data) {
        let token = accessToken;

        if (!token) {
            token = await refreshAccessToken();
            if (!token) {
                await logout();
                throw new Error("Session expired!");
            }
        }

        let response = await fetch(`${SERVER_URL}${endpoint}`, {
            headers: {
                'Authorization': `Bearer ${token}`
            },
            method: 'POST',
            credentials: 'include',
            body: JSON.stringify(data),
        });

        if (response.status === 401) {
            const refreshToken = refreshAccessToken();
            if (!refreshToken) {
                await logout();
                throw new Error("Session expired!");
            }

            response = await fetch(`${SERVER_URL}${endpoint}`, {
                headers: {
                    'Authorization': `Bearer ${accessToken}`
                },
                method: 'POST',
                credentials: 'include',
                body: JSON.stringify(data),
            });
        }
    }

    return { get, post, context };
}