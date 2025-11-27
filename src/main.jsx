import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App'
import './index.css'  // если нет файла, можешь удалить эту строку

ReactDOM.createRoot(document.getElementById('root')).render(
  <React.StrictMode>
    <App />
  </React.StrictMode>
)
