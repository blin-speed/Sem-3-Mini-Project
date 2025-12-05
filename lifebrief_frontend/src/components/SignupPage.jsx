import React, { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import "./SignupPage.css";

export default function SignupPage() {
  const [animate, setAnimate] = useState(false);
  const [message, setMessage] = useState(null);
  const navigate = useNavigate();

  const [formData, setFormData] = useState({
    email: "",
    username: "",
    password: "",
    city: "",
    state: "",
    country: "",
    age: "",
    weight: "",
    health: "",
    allergy: "",
    foodPref: "VEGETARIAN",
    days: [],
  });

  useEffect(() => {
    setTimeout(() => setAnimate(true), 50);
  }, []);

  const handleChange = (e) => {
    const { name, value, type, checked } = e.target;

    if (type === "checkbox") {
      setFormData((prev) => {
        const days = checked
          ? [...prev.days, value]
          : prev.days.filter((day) => day !== value);
        return { ...prev, days };
      });
    } else {
      setFormData((prev) => ({ ...prev, [name]: value }));
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await fetch("http://localhost:8080/api/auth/signup", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: formData.username,
          email: formData.email,
          password: formData.password,
          city: formData.city,
          state: formData.state,
          country: formData.country,
          age: formData.age,
          weight: formData.weight,
          foodPreference: formData.foodPref, // already matches enum
          healthProblems: formData.health,
          foodAllergies: formData.allergy,
          daysNonVeg: formData.days,
        }),
      });

      if (response.ok) {
        setMessage({ type: "success", text: "Signup successful! Please log in." });
        navigate("/login", { state: { fromSignup: true } });
      } else {
        const errorText = await response.text();
        setMessage({ type: "danger", text: errorText || "Signup failed. Please try again." });
      }
    } catch (error) {
      console.error("Signup error:", error);
      setMessage({ type: "danger", text: "Server error. Please try again later." });
    }
  };

  return (
    <div className="signup-wrapper">
      <div className={`signup-box ${animate ? "animate-in" : ""}`}>
        <h4 className="text-center text-white mb-4">Sign up for lifebrief</h4>

        {message && (
          <div className={`alert alert-${message.type}`}>{message.text}</div>
        )}

        <form onSubmit={handleSubmit} className="form-container">
          <h2 className="full-width">User Information</h2>

          <div>
            <label>Email</label>
            <input type="email" name="email" value={formData.email} onChange={handleChange} required />
          </div>

          <div>
            <label>Username</label>
            <input type="text" name="username" value={formData.username} onChange={handleChange} required />
          </div>

          <div>
            <label>Password</label>
            <input type="password" name="password" value={formData.password} onChange={handleChange} required />
          </div>

          <div>
            <label>City</label>
            <input type="text" name="city" value={formData.city} onChange={handleChange} />
          </div>

          <div>
            <label>State</label>
            <input type="text" name="state" value={formData.state} onChange={handleChange} />
          </div>

          <div>
            <label>Country</label>
            <input type="text" name="country" value={formData.country} onChange={handleChange} />
          </div>

          <div>
            <label>Age</label>
            <input type="number" name="age" value={formData.age} onChange={handleChange} />
          </div>

          <div>
            <label>Weight</label>
            <input type="number" name="weight" value={formData.weight} onChange={handleChange} />
          </div>

          <div>
            <label>Food Preference</label>
            <select name="foodPref" value={formData.foodPref} onChange={handleChange}>
              <option value="VEGETARIAN">Vegetarian</option>
              <option value="NON_VEGETARIAN">Non-Vegetarian</option>
              <option value="PARTIAL">Both Veg/Non-veg</option>
            </select>
          </div>

          <div className="full-width">
            <label>Health Problems</label>
            <textarea name="health" value={formData.health} onChange={handleChange}></textarea>
          </div>

          <div className="full-width">
            <label>Food Allergies</label>
            <textarea name="allergy" value={formData.allergy} onChange={handleChange}></textarea>
          </div>

          <div className="full-width">
            <label>Days you eat Non-Veg</label>
            <div className="checkbox-group">
              {["Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"].map((day) => (
                <label key={day}>
                  <input
                    type="checkbox"
                    value={day}
                    checked={formData.days.includes(day)}
                    onChange={handleChange}
                  />{" "}
                  {day}
                </label>
              ))}
            </div>
          </div>

          <button type="submit" className="full-width">Sign Up</button>
        </form>

        <Link to="/login" className="btn btn-link w-100 mt-3 text-white">
          Have an account? Log in instead
        </Link>
      </div>
    </div>
  );
}