import { useState } from "react";
import "./App.css";
import api from "./axios";

function App() {
  const [userName, setUserName] = useState("");
  const [id, setId] = useState("");
  const [pwd, setPwd] = useState("");

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    const { data } = await api({
      url: "/auth/login",
      method: "POST",
      data: {
        userName: id,
        password: pwd,
      },
    });

    if (data) {
      const { accessToken, refreshToken } = data;
      const expiresIn = 60 * 60 * 24; // 1 day

      document.cookie = `accessToken=${accessToken}; max-age=${expiresIn}; path=/`;
      document.cookie = `refreshToken=${refreshToken}; max-age=${expiresIn}; path=/`;
      alert("Login successful!");
    }
  };

  const handleGetUser = async () => {
    try {
      const { data } = await api({
        url: "/user",
        method: "GET",
        params: { userName: id },
      });

      setUserName(data.username);
    } catch (error) {
      console.error("Error fetching user data:", error);
      setUserName("");
    }
  };

  return (
    <div>
      <form>
        <input
          type="text"
          placeholder="ID"
          value={id}
          onChange={(e) => setId(e.target.value)}
        />
        <input
          placeholder="Password"
          value={pwd}
          onChange={(e) => setPwd(e.target.value)}
        />
        <button type="submit" onClick={handleSubmit}>
          Login
        </button>
      </form>
      <button onClick={handleGetUser}>getUser</button>
      <p>{userName}</p>
    </div>
  );
}

export default App;
