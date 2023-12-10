import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import TopNav from './components/TopNav/TopNav';
import FolderList from './components/FolderList/FolderList';
import MainPanel from './components/MainPanel/MainPanel';
import About from './components/About/About';
import Login from './components/Login/Login';
import './App.css';

function App() {
  return (
    <Router>
      <div className="App">
        <TopNav />
        <div className="container">
          <Routes>
            <Route path="/" element={<MainScreen />} />
            <Route path="/about" element={<About />} />
            <Route path="/login" element={<Login />} />
          </Routes>
        </div>
      </div>
    </Router>
  );
}

function MainScreen() {
  return (
    <div>
      <FolderList />
      <MainPanel />
    </div>
  );
}

export default App;