import express from "express";
import { createProxyMiddleware } from "http-proxy-middleware";
import path from "path";
import { fileURLToPath } from "url";

const app = express();
const PORT = 3000;

const __filename = fileURLToPath(import.meta.url);
const __dirname = path.dirname(__filename);

// ✅ Proxy primeiro
app.use(
  "/api",
  createProxyMiddleware({
    target: "http://localhost:8080",
    changeOrigin: true,
   pathRewrite: { "^/api": "" },
  })
);

// ✅ Arquivos estáticos
app.use("/src", express.static(path.join(__dirname, "src")));
app.use("/css", express.static(path.join(__dirname, "src", "static", "css")));
app.use("/js", express.static(path.join(__dirname, "src", "static", "js")));
app.use("/img", express.static(path.join(__dirname, "src", "static", "img")));
app.use("/video", express.static(path.join(__dirname, "src", "static", "video")));
app.use(express.static(path.join(__dirname, "src")));

app.get("/", (req, res) => {
  res.sendFile(path.join(__dirname, "index.html"));
});

app.listen(PORT, () => {
  console.log(`✅ Front rodando em: http://localhost:${PORT}`);
  console.log(`🔁 Proxy ativo: /api → http://localhost:8080`);
});

