// LoginPage.jsx
import React, { useState, useEffect } from 'react';
import { usePreviousPath } from "./RouteTracker";
import { Link, useNavigate, useLocation } from "react-router-dom";
import 'bootstrap/dist/css/bootstrap.min.css';

export default function LoginPage() {
  const [rememberMe, setRememberMe] = useState(false);
  const [animate, setAnimate] = useState(false);
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [message, setMessage] = useState(null);

  const [homepageMsgShown, setHomepageMsgShown] = useState(false);
  const [signupMsgShown, setSignupMsgShown] = useState(false);

  const navigate = useNavigate();
  const location = useLocation();

  const prevPath = usePreviousPath();
  const fromSignup = prevPath === "/signup";

  const handleLogin = async (e) => {
    e.preventDefault();
    setMessage(null);

    try {
      const response = await fetch("http://localhost:8080/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (response.ok) {
        const data = await response.json();

        const storage = rememberMe ? localStorage : sessionStorage;
        storage.setItem("token", data.token);
        storage.setItem("username", data.username);
        storage.setItem("email", data.email);
        storage.setItem("sessionId", String(data.sessionId));

        navigate("/homepage");
      } else {
        const err = await response.json().catch(() => ({}));
        setMessage({ type: "danger", text: err.error || "Invalid credentials. Please try again." });
      }
    } catch (error) {
      console.error("Login error:", error);
      setMessage({ type: "danger", text: "Server error. Please try again later." });
    }
  };

  // 🔑 Validate existing token on mount
  useEffect(() => {
    const token = localStorage.getItem("token") || sessionStorage.getItem("token");
    if (token) {
      fetch("http://localhost:8080/api/auth/validate", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ token })
      })
        .then(async res => {
          if (res.ok) {
            // Token still valid → skip login
            navigate("/homepage");
          } else {
            localStorage.clear();
            sessionStorage.clear();
            const err = await res.json().catch(() => ({}));
            setMessage({ type: "danger", text: err.error || "Session expired. Please log in again." });
          }
        })
        .catch(() => {
          setMessage({ type: "danger", text: "Server error during validation." });
        });
    }
  }, [navigate]);

  // Existing effect for logout/signup messages
  useEffect(() => {
    const timer = setTimeout(() => setAnimate(true), 50);

    if (location.state?.reason === "invalid" && !homepageMsgShown) {
      setHomepageMsgShown(true);
      setMessage({ type: "danger", text: "Invalid session. Please log in again." });
      localStorage.clear();
      sessionStorage.clear();
      navigate("/login", { replace: true, state: {} });
    }

    if (location.state?.reason === "logout" && !homepageMsgShown) {
      setHomepageMsgShown(true);
      setMessage({ type: "warning", text: "You have been logged out." });
      localStorage.clear();
      sessionStorage.clear();
      navigate("/login", { replace: true, state: {} });
    }

    if (location.state?.fromSignup && !signupMsgShown) {
      setSignupMsgShown(true);
      setMessage({ type: "success", text: "Welcome! Your account has been created. Please log in." });
      navigate(location.pathname, { replace: true, state: {} });
    }

    return () => clearTimeout(timer);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [prevPath, location]);

  return (
    <div className="container-fluid d-flex justify-content-center align-items-center my-4">
      <div
        className="p-4 rounded shadow d-flex flex-column justify-content-evenly"
        style={{
          backgroundColor: '#471396',
          minHeight: '400px',
          width: '400px',
          transform: animate
            ? "translateX(0)"
            : fromSignup
              ? "translateX(100px)"
              : "translateY(-100px)",
          opacity: animate ? 1 : 0,
          transition: 'all 0.6s ease',
        }}
      >
        <h4 className="text-center mb-3 text-white">Welcome back to lifebrief</h4>

        {message && (
          <div className={`alert alert-${message.type} mb-3`} role="alert">
            {message.text}
          </div>
        )}

        <form onSubmit={handleLogin}>
          <input
            type="text"
            placeholder="Username"
            value={username}
            onChange={(e) => setUsername(e.target.value)}
            className="form-control mb-3"
            style={{ backgroundColor: "#090040", color: "white", border: "1px solid #ffffffff" }}
            required
          />
          <input
            type="password"
            placeholder="Password"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            className="form-control mb-3"
            style={{ backgroundColor: "#090040", color: "white", border: "1px solid #cfcfe6" }}
            required
          />

          <div className="d-flex justify-content-between align-items-center my-2">
            <div className="form-check">
              <input
                className="form-check-input"
                type="checkbox"
                id="rememberMe"
                checked={rememberMe}
                onChange={(e) => setRememberMe(e.target.checked)}
              />
              <label className="form-check-label text-white ms-1" htmlFor="rememberMe">
                Remember me
              </label>
            </div>
            <button type="button" className="btn btn-link p-0" style={{ color: "#00ffc6" }}>
              Forgot password?
            </button>
          </div>

          <button
            type="submit"
            className="btn w-100 mb-3"
            style={{ backgroundColor: "#00ffc6", color: "#090040", fontWeight: "600" }}
          >
            Log in
          </button>
        </form>

        <Link to="/signup" className="btn btn-link p-0" style={{ color: "#00ffc6" }}>
          Are you new here?
        </Link>
      </div>
    </div>
  );
}