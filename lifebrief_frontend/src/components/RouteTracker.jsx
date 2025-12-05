// src/RouteTracker.jsx
import { createContext, useContext, useRef, useEffect } from "react";
import { useLocation } from "react-router-dom";

const RouteContext = createContext();

export function RouteProvider({ children }) {
    const location = useLocation();
    const prevPath = useRef(null);

    useEffect(() => {
        prevPath.current = location.pathname;
    }, [location]);

    return (
        <RouteContext.Provider value={prevPath.current}>
            {children}
        </RouteContext.Provider>
    );
}

export function usePreviousPath() {
    return useContext(RouteContext);
}