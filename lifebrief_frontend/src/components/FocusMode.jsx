// FocusMode.jsx
import React, { useEffect, useMemo, useRef, useState } from "react";
import { useNavigate } from "react-router-dom";

const COLORS = {
  base0: "#090040",
  base1: "#471396",
  base2: "#2a0a5e",
  white: "#ffffff",
  gray: "#cccccc",
  aqua: "#00ffc6",
  teal: "#00d9a8",
};

export default function FocusMode() {
  const navigate = useNavigate();

  const exercise = {
    id: "breathing",
    name: "Focus your mind",
    durationSec: 180,
  };

  const [isPlaying, setIsPlaying] = useState(false);
  const [elapsed, setElapsed] = useState(0);
  const total = exercise.durationSec;
  const progress = Math.min(elapsed / total, 1);

  const rafRef = useRef(null);
  const lastTickRef = useRef(0);

  // Timer loop
  useEffect(() => {
    if (!isPlaying) return;
    const loop = (ts) => {
      if (!lastTickRef.current) lastTickRef.current = ts;
      const delta = (ts - lastTickRef.current) / 1000;
      if (delta >= 0.25) {
        setElapsed((prev) => Math.min(prev + delta, total));
        lastTickRef.current = ts;
      }
      rafRef.current = requestAnimationFrame(loop);
    };
    rafRef.current = requestAnimationFrame(loop);
    return () => cancelAnimationFrame(rafRef.current);
  }, [isPlaying, total]);


  // Breathing rhythm: 4s inhale, 2s hold, 6s exhale
  const cycle = 12;
  const t = elapsed % cycle;
  let breathingPhase = "Exhale";
  let scale = 0.9;
  if (t < 4) {
    breathingPhase = "Inhale";
    scale = 0.9 + (t / 4) * (1.1 - 0.9); // 0.9 -> 1.1
  } else if (t < 6) {
    breathingPhase = "Hold";
    scale = 1.1;
  } else {
    breathingPhase = "Exhale";
    scale = 1.1 - ((t - 6) / 6) * (1.1 - 0.9); // 1.1 -> 0.9
  }

  const togglePlay = () => setIsPlaying((p) => !p);
  const endSession = () => navigate("/HomePage");

  const mins = Math.floor(elapsed / 60);
  const secs = Math.floor(elapsed % 60);

  return (
    <main
      style={{
        minHeight: "100vh",
        //background: `linear-gradient(135deg, ${COLORS.base0}, ${COLORS.base2}, ${COLORS.base1})`,
        color: COLORS.white,
        display: "flex",
        flexDirection: "column",
        alignItems: "center",
        justifyContent: "center",
        textAlign: "center",
        padding: "40px 20px",
      }}
    >
      {/* Removed CSS animation, now controlled by React state */}

      {/* Centered header */}
      <header
        style={{
          width: "120%",
          textAlign: "center",
          fontSize: "1.6rem",
          fontWeight: 700,
          letterSpacing: "0.04em",
          marginBottom: "32px",
          marginTop: "0",
        }}
      >
        Focus Mode
      </header>

      {/* Meditation content */}
      <div style={{ Width: "1000px", width: "100%" }}>
        <h1 style={{ fontSize: "2rem", fontWeight: 700, marginBottom: "16px" }}>
          {exercise.name}
        </h1>

        <p style={{ color: COLORS.gray, marginBottom: "24px" }}>
          {mins}:{secs.toString().padStart(2, "0")} elapsed
        </p>

        {/* Circle breathing guide */}
        <div
          className="breathing-circle"
          style={{
            width: "180px",
            height: "180px",
            borderRadius: "50%",
            background: `radial-gradient(circle at 45% 45%, ${COLORS.aqua}, ${COLORS.teal})`,
            margin: "30px auto",
            transform: `scale(${scale})`,
            transition: "transform 0.2s linear",
          }}
        />

        {/* Progress percentage */}
        <p style={{ marginTop: "20px", color: COLORS.gray }}>
          {Math.round(progress * 100)}% complete
        </p>

        {/* Single word phase */}
        <p style={{ marginTop: "28px", fontSize: "1.6rem", fontWeight: 600 }}>
          {breathingPhase}
        </p>

        {/* Controls */}
        <div
          style={{
            marginTop: "36px",
            display: "flex",
            gap: "20px",
            justifyContent: "center",
          }}
        >
          <button
            className="btn"
            style={{
              backgroundColor: COLORS.aqua,
              color: COLORS.base0,
              fontWeight: 600,
              padding: "10px 24px",
            }}
            onClick={togglePlay}
          >
            {isPlaying ? "Pause" : "Play"}
          </button>
          <button
            className="btn btn-outline-light"
            style={{
              borderColor: COLORS.gray,
              color: COLORS.gray,
              padding: "10px 24px",
            }}
            onClick={endSession}
          >
            End Session
          </button>
        </div>
      </div>
    </main>
  );
}