import React, { useState, useEffect } from "react";
import { FaPencilAlt } from "react-icons/fa";
import "./ProfilePage.css";

function EditableField({ label, field, value, editingField, setEditingField, onSave }) {
  const [tempValue, setTempValue] = useState(value || "");

  return (
    <div className="profile-field">
      <label className="profile-label">{label}:</label>
      {editingField === field ? (
        <div className="edit-controls">
          <input
            className="profile-input"
            type="text"
            value={tempValue}
            onChange={(e) => setTempValue(e.target.value)}
          />
          <button className="btn btn-save" onClick={() => onSave(field, tempValue)}>Save</button>
          <button className="btn btn-cancel" onClick={() => setEditingField(null)}>Cancel</button>
        </div>
      ) : (
        <div className="field-display">
          <span>{value || "—"}</span>
          <FaPencilAlt
            onClick={() => setEditingField(field)}
            className="edit-icon"
          />
        </div>
      )}
    </div>
  );
}

export default function ProfilePage() {
  const [profile, setProfile] = useState(null);
  const [editingField, setEditingField] = useState(null);
  const [error, setError] = useState(null);

  useEffect(() => {
    const username = sessionStorage.getItem("username");
    if (!username) {
      setError("No username found in localStorage");
      return;
    }

    fetch(`http://localhost:8080/api/profiles/${username}`)
      .then(res => {
        if (res.status === 404) throw new Error("Profile not found");
        if (!res.ok) throw new Error("Failed to fetch profile");
        return res.json();
      })
      .then(data => setProfile(data))
      .catch(err => setError(err.message));
  }, []);

  const handleSave = (field, value) => {
    fetch(`http://localhost:8080/api/profiles/${profile.profileId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ ...profile, [field]: value })
    })
      .then(res => {
        if (!res.ok) throw new Error("Failed to update profile");
        return res.json();
      })
      .then(updated => {
        setProfile(updated);
        setEditingField(null);
      })
      .catch(err => setError(err.message));
  };

  if (error) return <div className="error">⚠️ {error}</div>;
  if (!profile) return <div className="loading">Loading profile...</div>;

  return (
    <div className="profile-wrapper">
      <h2 className="profile-title">My Profile</h2>

      {/* Account Info */}
      <section className="profile-section">
        <h3 className="section-title">Account Info</h3>
        <div className="profile-field">
          <label className="profile-label">Username:</label>
          <span>{profile.username}</span>
        </div>
        <EditableField label="Email" field="email" value={profile.email}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <div className="profile-field">
          <label className="profile-label">Password:</label>
          <span>••••••••</span>
          <button className="btn btn-change">Change</button>
        </div>
      </section>

      {/* Personal Details */}
      <section className="profile-section">
        <h3 className="section-title">Personal Details</h3>
        <EditableField label="City" field="city" value={profile.city}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <EditableField label="State" field="state" value={profile.state}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <EditableField label="Country" field="country" value={profile.country}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <EditableField label="Age" field="age" value={profile.age}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <EditableField label="Weight" field="weight" value={profile.weight}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
      </section>

      {/* Preferences & Health */}
      <section className="profile-section">
        <h3 className="section-title">Preferences & Health</h3>
        <EditableField label="Food Preference" field="foodPreference" value={profile.foodPreference}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <EditableField label="Health Problems" field="healthProblems" value={profile.healthProblems}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
        <EditableField label="Food Allergies" field="foodAllergies" value={profile.foodAllergies}
          editingField={editingField} setEditingField={setEditingField} onSave={handleSave} />
      </section>
    </div>
  );
}