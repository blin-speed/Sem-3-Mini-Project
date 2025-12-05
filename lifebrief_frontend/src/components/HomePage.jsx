import React from "react";
import { useNavigate, Link } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./HomePage.css";

export default function HomePage() {
    const navigate = useNavigate();

    const cards = [
        { 
            title: "Health Chat", 
            desc: "Your AI wellness partner for fitness, nutrition, and lifestyle guidance." 
        },
        { 
            title: "Focus Mode", 
            desc: "Switch between Task Mode to get work done and Meditation Mode to recharge your mind." 
        },
        { 
            title: "Task Planner", 
            desc: "Plan, organize, and visualize your daily tasks with a clean, structured layout." 
        }
    ];

    const handleLogout = async () => {
        const token = localStorage.getItem("token") || sessionStorage.getItem("token");

        if (!token) {
            navigate("/login", { state: { reason: "invalid" } });
            return;
        }

        try {
            const response = await fetch("http://localhost:8080/api/auth/logout", {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ token })
            });

            localStorage.clear();
            sessionStorage.clear();

            if (response.ok) {
                navigate("/login", { state: { reason: "logout" } });
            } else {
                const err = await response.json().catch(() => ({}));
                navigate("/login", { state: { reason: "invalid", error: err.error } });
            }
        } catch (error) {
            console.error("Logout error:", error);
            navigate("/login", { state: { reason: "invalid" } });
        }
    };

    return (
        <>
            <div className="page-header d-flex justify-content-between align-items-center px-3 py-2">
                {/* Left: Logout */}
                <button
                    className="btn btn-outline-light"
                    onClick={handleLogout}
                >
                    Logout
                </button>

                {/* Center: Welcome */}
                <h1 className="text-center m-0">Welcome to Your Dashboard</h1>

                {/* Right: Profile circle */}
                <Link to="/profile" className="profile-circle circle-outline-light d-flex align-items-center justify-content-center">
                    <span className="profile-initial">
                        {localStorage.getItem("username")?.charAt(0).toUpperCase() ||
                            sessionStorage.getItem("username")?.charAt(0).toUpperCase() ||
                            "U"}
                    </span>
                </Link>
            </div>

            <div className="container-fluid text-center my-5">
                <div className="tile-row">
                    {cards.map((card, index) => (
                        <div 
                            key={index} 
                            className="tile"
                            onClick={() => {
                                if (index === 0) navigate("/healthchat");
                                if (index === 1) navigate("/focus-mode");   // ✅ Updated to Focus Mode
                                if (index === 2) navigate("/task-planner");
                            }}
                            style={{ cursor: "pointer" }}
                        >
                            <div className="tile-content">
                                <div className="tile-title">{card.title}</div>
                                <div className="tile-desc">{card.desc}</div>
                            </div>
                        </div>
                    ))}
                </div>
            </div>
        </>
    );
}