# Hytale Local Server Setup

Este guia explica como subir um servidor Hytale localmente para
desenvolvimento de mods/plugins em Java.

Refer: [Support Hytale](https://support.hytale.com/hc/en-us/articles/45326769420827-Hytale-Server-Manual#:~:text=mixed%20mode%2C%20sharing)

------------------------------------------------------------------------

## 📦 Pré-requisitos

-   Java 25 instalado
-   Conta Hytale válida
-   Cliente Hytale instalado
-   Sistema operacional: Windows

Verifique sua versão do Java:

``` bash
java --version
```

Deve retornar algo como:

    openjdk 25.x.x

------------------------------------------------------------------------

## 📁 Estrutura de Pastas

Crie uma pasta para o servidor:

    hytale-server/

Dentro dela teremos:

    hytale-server/
     ├── Server/
     └── Assets.zip

------------------------------------------------------------------------

## 🚀 Obtendo os Arquivos do Servidor

Você tem duas opções:

### Opção 1 --- Copiar do cliente instalado

Local padrão:

### Windows

    %appdata%\Hytale\install\release\package\game\latest

Copie: - `Server/` - `Assets.zip`

Para sua pasta `hytale-server/`.

------------------------------------------------------------------------

## ▶️ Iniciando o Servidor

Abra o terminal dentro da pasta:

    hytale-server/Server

Execute:

``` bash
java -XX:AOTCache=HytaleServer.aot -jar HytaleServer.jar --assets ../Assets.zip
```

------------------------------------------------------------------------

## 🔐 Autenticando o Servidor (Primeira Execução)

Ao iniciar pela primeira vez, o servidor exigirá autenticação.

No console do servidor, execute:

    /auth login device

O servidor mostrará um código e uma URL.

1.  Abra a URL no navegador.
2.  Faça login com sua conta Hytale.
3.  Insira o código exibido.

Após isso, o servidor ficará autorizado.

------------------------------------------------------------------------

## 🌍 Conectando ao Servidor

Com o servidor rodando:

1.  Abra o cliente Hytale.
2.  Inicie o Hytale.
3.  Adicione um servidor com:

-   Endereço: `localhost`
-   Porta padrão: `5520`

------------------------------------------------------------------------

## 🧩 Instalando Mods

Coloque seu `.jar` dentro da pasta:

    Server/mods/

Exemplo:

    Server/mods/orb-luminous-mod-1.0.jar

Reinicie o servidor após adicionar ou atualizar mods.

------------------------------------------------------------------------

## 📂 Estrutura Gerada Após Inicialização

Após rodar o servidor, serão criadas pastas como:

    Server/
     ├── logs/
     ├── mods/
     ├── universe/
     ├── config.json
     ├── permissions.json
     ├── whitelist.json
     └── bans.json

------------------------------------------------------------------------

## 🛑 Parando o Servidor

No console:

    /stop

Não feche o terminal à força.

------------------------------------------------------------------------

## 🎯 Objetivo do Servidor Local

O servidor local permite:

-   Testar mods em desenvolvimento
-   Criar universos customizados
-   Executar lógica server-side

------------------------------------------------------------------------

## ⚠️ Observações Importantes

-   O servidor exige Java 25.
-   Não há hot reload seguro de mods.
-   Sempre reinicie após alterações no `.jar`.

------------------------------------------------------------------------

## ✅ Ambiente Pronto

Se tudo estiver correto:

-   O servidor inicia sem erros
-   Você consegue conectar via `localhost`
-   Seu mod aparece no log ao iniciar

Agora você está pronto para desenvolver e testar seu plugin.

## 👨‍💻 Autor
Aryosvalldo Cleef ─ [linkedin](https://www.linkedin.com/in/aryosvalldo-cleef/) ─ [@cleefsouza](https://github.com/cleefsouza)

## ✍️ Meta
Made with 💜 by **Cleef Souza**