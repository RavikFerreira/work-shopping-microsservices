# 🛒 Work Shopping Microservices

Sistema de e-commerce desenvolvido com arquitetura de microsserviços, focado em escalabilidade, desacoplamento entre serviços e facilidade de manutenção.

O projeto foi criado com o objetivo de demonstrar conceitos modernos de desenvolvimento backend distribuído, integração entre serviços e utilização de boas práticas de arquitetura.

---

# 📚 Sumário

* [Sobre o Projeto](#-sobre-o-projeto)
* [Visualização do Processo BPMN](#-visualização-do-processo-bpmn)
* [Arquitetura](#-arquitetura)
* [Tecnologias Utilizadas](#-tecnologias-utilizadas)
* [Estrutura do Projeto](#-estrutura-do-projeto)
* [Microsserviços](#-microsserviços)
* [Pré-requisitos](#-pré-requisitos)
* [Como Executar o Projeto](#-como-executar-o-projeto)
* [Docker](#-docker)
* [Endpoints](#-endpoints)
* [Boas Práticas Aplicadas](#-boas-práticas-aplicadas)
* [Melhorias Futuras](#-melhorias-futuras)
* [Autor](#-autor)

---

# 📖 Sobre o Projeto

O **Work Shopping Microservices** é uma aplicação baseada em microsserviços voltada para um cenário de marketplace/e-commerce.

A proposta da aplicação é separar responsabilidades em serviços independentes, permitindo:

* Escalabilidade horizontal;
* Facilidade de manutenção;
* Resiliência;
* Melhor organização do domínio;
* Comunicação desacoplada entre serviços.

O projeto segue conceitos de arquitetura distribuída e pode ser utilizado como base de estudos para:

* Microsserviços;
* Docker;
* APIs REST;
* Mensageria;
* Observabilidade.

---

# 🗺️ Visualização do Processo BPMN

Abaixo está a representação visual do fluxo de negócio desenhado para esta arquitetura:

<p align="center">
  <img src="./processo.png" alt="Diagrama BPMN" width="100%">
</p>

---

# 🏗 Arquitetura

A aplicação foi estruturada utilizando arquitetura modular baseada em microsserviços.

Cada serviço possui responsabilidade específica e comunicação via APIs REST.

```text
Cliente
   ↓
Aplicação
   ↓
---------------------------------

| Product Service              |
| Order Service                |
| Payment Service               |
---------------------------------
   ↓
Banco de dados
