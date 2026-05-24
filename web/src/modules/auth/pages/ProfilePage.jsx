import { useEffect, useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import Navbar from "../../../shared/components/Navbar.jsx";
import { useAuth } from "../context/AuthContext.jsx";
import { fetchProfile, updateProfile, changePassword } from "../api/profileApi.js";
import "./ProfilePage.css";

const TABS = {
  INFO: "info",
  SECURITY: "security",
};

export default function ProfilePage() {
  const { user, logout, login } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState(TABS.INFO);
  const [loading, setLoading] = useState(true);
  const [profile, setProfile] = useState(null);
  const [infoForm, setInfoForm] = useState({ firstname: "", lastname: "", phone: "" });
  const [passwordForm, setPasswordForm] = useState({ currentPassword: "", newPassword: "", confirmPassword: "" });
  const [message, setMessage] = useState(null);

  useEffect(() => {
    document.title = "AquaHaven | My Profile";
    loadProfile();
  }, []);

  const loadProfile = async () => {
    try {
      const data = await fetchProfile();
      setProfile(data);
      setInfoForm({ firstname: data.firstname, lastname: data.lastname, phone: data.phone || "" });
    } catch (err) {
      console.error("Failed to load profile", err);
    } finally {
      setLoading(false);
    }
  };

  const handleInfoSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
    try {
      const updated = await updateProfile(infoForm);
      setProfile(updated);
      login(updated, sessionStorage.getItem("accessToken"), sessionStorage.getItem("refreshToken"));
      setMessage({ type: "success", text: "Profile updated successfully!" });
    } catch (err) {
      setMessage({ type: "error", text: err.response?.data?.message || "Failed to update profile" });
    }
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    setMessage(null);
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      setMessage({ type: "error", text: "Passwords do not match" });
      return;
    }
    if (passwordForm.newPassword.length < 6) {
      setMessage({ type: "error", text: "New password must be at least 6 characters" });
      return;
    }
    try {
      await changePassword({
        currentPassword: passwordForm.currentPassword,
        newPassword: passwordForm.newPassword,
      });
      setPasswordForm({ currentPassword: "", newPassword: "", confirmPassword: "" });
      setMessage({ type: "success", text: "Password changed successfully!" });
    } catch (err) {
      setMessage({ type: "error", text: err.response?.data?.message || "Failed to change password" });
    }
  };

  const handleLogout = () => {
    logout();
    navigate("/", { replace: true });
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <main className="profile-page" style={{ marginTop: "var(--navbar-h)" }}>
          <div className="profile-container">
            <div className="profile-header">
              <h1>My Profile</h1>
            </div>
            <div className="profile-loading">Loading profile...</div>
          </div>
        </main>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <main className="profile-page" style={{ marginTop: "var(--navbar-h)" }}>
        <div className="profile-container">
          <div className="profile-header">
            <h1>My Profile</h1>
          </div>

          {message && (
            <div className={`profile-message profile-message--${message.type}`}>
              {message.text}
            </div>
          )}

          <div className="profile-layout">
            <div className="profile-sidebar">
              <div className="profile-avatar-large">
                <span>{profile?.firstname?.[0]?.toUpperCase()}{profile?.lastname?.[0]?.toUpperCase()}</span>
              </div>
              <h3 className="profile-name">{profile?.firstname} {profile?.lastname}</h3>
              <p className="profile-email">{profile?.email}</p>

              <div className="profile-nav">
                <button
                  className={`profile-nav__btn ${activeTab === TABS.INFO ? "profile-nav__btn--active" : ""}`}
                  onClick={() => setActiveTab(TABS.INFO)}
                >
                  Personal Info
                </button>
                <button
                  className={`profile-nav__btn ${activeTab === TABS.SECURITY ? "profile-nav__btn--active" : ""}`}
                  onClick={() => setActiveTab(TABS.SECURITY)}
                >
                  Security
                </button>
                <Link to="/orders" className="profile-nav__btn">My Orders</Link>
                <button onClick={handleLogout} className="profile-nav__btn profile-nav__btn--logout">
                  Log Out
                </button>
              </div>
            </div>

            <div className="profile-content">
              {activeTab === TABS.INFO && (
                <div className="profile-tab">
                  <h2 className="profile-tab__title">Personal Information</h2>
                  <form className="profile-form" onSubmit={handleInfoSubmit}>
                    <div className="profile-form__group">
                      <label htmlFor="firstname">First Name</label>
                      <input
                        id="firstname"
                        type="text"
                        value={infoForm.firstname}
                        onChange={(e) => setInfoForm({ ...infoForm, firstname: e.target.value })}
                      />
                    </div>
                    <div className="profile-form__group">
                      <label htmlFor="lastname">Last Name</label>
                      <input
                        id="lastname"
                        type="text"
                        value={infoForm.lastname}
                        onChange={(e) => setInfoForm({ ...infoForm, lastname: e.target.value })}
                      />
                    </div>
                    <div className="profile-form__group">
                      <label htmlFor="phone">Phone Number</label>
                      <input
                        id="phone"
                        type="tel"
                        value={infoForm.phone}
                        onChange={(e) => setInfoForm({ ...infoForm, phone: e.target.value })}
                      />
                    </div>
                    <div className="profile-form__group">
                      <label>Email</label>
                      <input type="email" value={profile?.email} disabled />
                    </div>
                    <button type="submit" className="profile-form__btn">Save Changes</button>
                  </form>
                </div>
              )}

              {activeTab === TABS.SECURITY && (
                <div className="profile-tab">
                  <h2 className="profile-tab__title">Change Password</h2>
                  <form className="profile-form" onSubmit={handlePasswordSubmit}>
                    <div className="profile-form__group">
                      <label htmlFor="currentPassword">Current Password</label>
                      <input
                        id="currentPassword"
                        type="password"
                        value={passwordForm.currentPassword}
                        onChange={(e) => setPasswordForm({ ...passwordForm, currentPassword: e.target.value })}
                        required
                      />
                    </div>
                    <div className="profile-form__group">
                      <label htmlFor="newPassword">New Password</label>
                      <input
                        id="newPassword"
                        type="password"
                        value={passwordForm.newPassword}
                        onChange={(e) => setPasswordForm({ ...passwordForm, newPassword: e.target.value })}
                        required
                      />
                    </div>
                    <div className="profile-form__group">
                      <label htmlFor="confirmPassword">Confirm New Password</label>
                      <input
                        id="confirmPassword"
                        type="password"
                        value={passwordForm.confirmPassword}
                        onChange={(e) => setPasswordForm({ ...passwordForm, confirmPassword: e.target.value })}
                        required
                      />
                    </div>
                    <button type="submit" className="profile-form__btn">Update Password</button>
                  </form>
                </div>
              )}
            </div>
          </div>
        </div>
      </main>
    </>
  );
}
