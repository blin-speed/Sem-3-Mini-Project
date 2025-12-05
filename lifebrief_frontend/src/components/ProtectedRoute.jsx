import React, { useEffect, useState } from "react";
import { Navigate } from "react-router-dom";

export default function ProtectedRoute({ children }) {
  const [valid, setValid] = useState(null);
  const token = localStorage.getItem("token") || sessionStorage.getItem("token");

  useEffect(() => {
    if (!token) {
      setValid(false);
      return;
    }

    fetch("http://localhost:8080/api/auth/validate", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ token })
    })
      .then(res => setValid(res.ok))
      .catch(() => setValid(false));
  }, [token]);

  if (valid === null) {
    return <div>Loading...</div>; // or a spinner
  }

  if (!valid) {
    return <Navigate to="/login" replace state={{ reason: "invalid" }} />;
  }

  return children;
}