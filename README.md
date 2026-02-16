<p align="center">
  <img width="500" height="500" alt="logo-lumos-mod" src="https://github.com/user-attachments/assets/e3e83b69-60e0-4cd4-89d6-2da8f05322d0" />
</p>

# Hytale Lumos Mod ✨
Mod para **Hytale** que adiciona um efeito de **luz envolvendo o player**, deixando o caminho mais visível durante a exploração. Para quem joga de espada+escudo, adagas duplas ou está com as duas mãos ocupadas.

https://github.com/user-attachments/assets/5094c031-841d-40da-9304-1c696c709a40

---

## ✨ Funcionalidades

> _Antes de tudo, execute_ `/op self`

- Luz/aura dinâmica ao redor do jogador para melhorar a visibilidade
- Comando para **ligar/desligar** quando quiser:
  - `/lumos on`
  - `/lumos off`
- Foco em ser **simples, leve e direto ao ponto**

---

## ✅ Requisitos

- **Java 25**
- **Servidor Hytale** (para rodar mods)
- **Cliente Hytale** (para conectar e testar)

> _Dica: confira sua versão do Java com:_ `java --version`

---

## 📦 Instalação (Servidor)

> _Ainda não há releases publicadas, então o caminho mais comum é **buildar** o `.jar` e colocar no servidor_

1. Gere o `.jar` do mod (veja **Build** abaixo)
2. Copie o arquivo `.jar` para a pasta de mods do servidor: `Server/mods/`

Exemplo:
> Server/mods/hytale-lumos-mod-1.0-SNAPSHOT.jar

3. Reinicie o servidor
> _Não há hot reload seguro — reinicie após atualizar o_ `.jar`

---

## 🎮 Como usar

Dentro do jogo (no chat), use:

> _Antes de tudo, execute_ `/op self`

```txt
/lumos on
/lumos off
```

---

## 🛠️ Build (Maven)

### 1) Dependência local do servidor
Este projeto usa o `HytaleServer.jar` localmente via `./libs/`.

Coloque o arquivo em:
```
libs/HytaleServer.jar
```

### 2) Compilar
```bash
mvn clean package
```

O `.jar` final será gerado em:
```
target/
```

Depois disso, é só copiar o `.jar` para `Server/mods/` e reiniciar o servidor.

---

## 🧪 Rodando servidor local (dev)

O repositório contém um guia de setup local do servidor:

- **SERVER_README.md** → como copiar `Server/` e `Assets.zip`, iniciar o servidor, autenticar e conectar via `localhost`.

Atalho (o essencial):
- Iniciar servidor:
```bash
java -XX:AOTCache=HytaleServer.aot -jar HytaleServer.jar --assets ../Assets.zip
```
- Autenticar (primeira vez):
```txt
/auth login device
```
- Parar servidor:
```txt
/stop
```
---

## ⚙️ Configuração

> _No momento, sem arquivo de configuração apenas comandos_ `/lumos on|off`

---

## 🧩 Troubleshooting

Se o mod não carregar:

- Confirme que você está usando **Java 25**
- Confirme o caminho do `.jar`:
    - `Server/mods/hytale-lumos-mod-1.0-SNAPSHOT.jar`
- Reinicie o servidor após adicionar/atualizar o mod
- Verifique `Server/logs/` para mensagens de erro

---

## 🤝 Contribuindo

Quer me ajudar a evoluir o plugin/mod, segue algumas boas ideias:
- Intensidade/raio da luz
- Animação ao ativar
- Usar um item para ativar

Como contribuir?

1. Faça um fork
2. Crie uma branch:
```bash
git checkout -b feat/sua-feature
```
3. Commit:
```bash
git commit -m "[Feat]: Breve descrição da feature"
```
4. Push e Pull Request

---

## 📄 Licença

Ainda não definida :/

---

## 👨‍💻 Autor
Aryosvalldo Cleef ─ [linkedin](https://www.linkedin.com/in/aryosvalldo-cleef/) ─ [@cleefsouza](https://github.com/cleefsouza)

## ✍️ Meta
Made with 💜 by **Cleef Souza | Pepola**
