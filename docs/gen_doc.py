# -*- coding: utf-8 -*-
"""Gera a documentacao da entrega Domain Driven Design - Java (GS CitizenTech 2026.2)."""
import os
from docx import Document
from docx.shared import Pt, Cm, RGBColor
from docx.enum.text import WD_ALIGN_PARAGRAPH
from docx.enum.table import WD_TABLE_ALIGNMENT
from docx.oxml.ns import qn
from docx.oxml import OxmlElement

BASE = os.path.dirname(os.path.abspath(__file__))
PACOTE = os.path.join(BASE, "..", "..", "Pacote_Java_WomansShield", "pacote_java_womans_shield")
TELAS = os.path.join(PACOTE, "telas")
DER_IMG = os.path.join(PACOTE, "der", "diagrama_modelo_logico.png")
DIAGRAMA_CLASSES_IMG = os.path.join(BASE, "diagrama-classes.png")

AZUL = RGBColor(0x1F, 0x3A, 0x5F)

INTEGRANTES = [
    ("Leonardo Eiji Kina", "562784"),
    ("Nicholas Braga de Souza", "561733"),
    ("Tome Rossi Giani", "562422"),
    ("Vitor Ramos de Farias", "561958"),
]

GITHUB_URL = "https://github.com/leo-kina/GSJava"
DEPLOY_URL = "(A PREENCHER apos o deploy no Render)"

ENDPOINTS = [
    ("GET", "/bairros", "Lista os bairros (id, nome, indice de risco)", "200"),
    ("GET", "/usuarias", "Lista as usuarias", "200"),
    ("GET", "/usuarias/{id}", "Busca uma usuaria", "200"),
    ("POST", "/usuarias", "Cadastra usuaria", "201"),
    ("PUT", "/usuarias/{id}", "Altera usuaria", "200"),
    ("DELETE", "/usuarias/{id}", "Desativa a usuaria (ST_ATIVA='N'), sem apagar o historico", "204"),
    ("GET", "/usuarias/{id}/contatos", "Lista os contatos da usuaria (principal primeiro)", "200"),
    ("POST", "/usuarias/{id}/contatos", "Cadastra contato de confianca", "201"),
    ("GET", "/contatos/{id}", "Busca um contato", "200"),
    ("PUT", "/contatos/{id}", "Altera contato (telefone em branco mantem o atual)", "200"),
    ("DELETE", "/contatos/{id}", "Remove contato", "204"),
    ("POST", "/alertas", "Registra um pedido de ajuda e os contatos avisados", "201"),
    ("GET", "/alertas?idUsuaria=1", "Historico de alertas da usuaria", "200"),
    ("GET", "/alertas/{id}", "Um alerta com os contatos avisados, em ordem", "200"),
    ("DELETE", "/alertas/{id}", "Apaga um alerta do historico", "204"),
]

ERROS = [
    ("Campo obrigatorio vazio ou invalido", "400"),
    ("Usuaria, contato, bairro ou alerta nao encontrado", "404"),
    ("Telefone ja cadastrado para a usuaria; limite de 6 contatos; contato com historico de alertas", "409"),
    ("Alerta sem nenhum contato, ou usuaria desativada", "422"),
]


def set_cell_background(cell, color_hex):
    shd = OxmlElement('w:shd')
    shd.set(qn('w:fill'), color_hex)
    cell._tc.get_or_add_tcPr().append(shd)


def heading(doc, text, level=1):
    h = doc.add_heading(text, level=level)
    for run in h.runs:
        run.font.color.rgb = AZUL
    return h


def add_table(doc, header, rows, widths=None):
    table = doc.add_table(rows=1, cols=len(header))
    table.style = "Light Grid Accent 1"
    table.alignment = WD_TABLE_ALIGNMENT.CENTER
    hdr_cells = table.rows[0].cells
    for i, text in enumerate(header):
        hdr_cells[i].text = text
        for p in hdr_cells[i].paragraphs:
            for r in p.runs:
                r.bold = True
        set_cell_background(hdr_cells[i], "1F3A5F")
        for p in hdr_cells[i].paragraphs:
            for r in p.runs:
                r.font.color.rgb = RGBColor(0xFF, 0xFF, 0xFF)
    for row in rows:
        cells = table.add_row().cells
        for i, value in enumerate(row):
            cells[i].text = str(value)
    if widths:
        for row in table.rows:
            for i, w in enumerate(widths):
                row.cells[i].width = Cm(w)
    return table


def add_image_centered(doc, path, width_cm=15.5, caption=None):
    if os.path.exists(path):
        doc.add_picture(path, width=Cm(width_cm))
        doc.paragraphs[-1].alignment = WD_ALIGN_PARAGRAPH.CENTER
    else:
        p = doc.add_paragraph(f"[Imagem nao encontrada: {path}]")
        p.runs[0].font.color.rgb = RGBColor(0xC0, 0x00, 0x00)
    if caption:
        cap = doc.add_paragraph(caption)
        cap.alignment = WD_ALIGN_PARAGRAPH.CENTER
        cap.runs[0].italic = True
        cap.runs[0].font.size = Pt(10)


def main():
    doc = Document()

    style = doc.styles["Normal"]
    style.font.name = "Calibri"
    style.font.size = Pt(11)

    # ---------- CAPA ----------
    for _ in range(4):
        doc.add_paragraph()
    titulo = doc.add_paragraph()
    titulo.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = titulo.add_run("Woman's Shield")
    run.bold = True
    run.font.size = Pt(32)
    run.font.color.rgb = AZUL

    subtitulo = doc.add_paragraph()
    subtitulo.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = subtitulo.add_run("API de Contatos de Confianca e Alertas")
    run.font.size = Pt(18)

    doc.add_paragraph()
    linha = doc.add_paragraph()
    linha.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = linha.add_run("Global Solution 2026.2 - CitizenTech\nDomain Driven Design - Java")
    run.font.size = Pt(14)

    for _ in range(6):
        doc.add_paragraph()

    integrantes_p = doc.add_paragraph()
    integrantes_p.alignment = WD_ALIGN_PARAGRAPH.CENTER
    run = integrantes_p.add_run("Integrantes")
    run.bold = True
    run.font.size = Pt(13)
    for nome, rm in INTEGRANTES:
        p = doc.add_paragraph(f"{nome} - RM {rm}")
        p.alignment = WD_ALIGN_PARAGRAPH.CENTER

    for _ in range(4):
        doc.add_paragraph()
    rodape = doc.add_paragraph("FIAP - Engenharia de Software - Turma Lins/Paulista - 2026")
    rodape.alignment = WD_ALIGN_PARAGRAPH.CENTER

    doc.add_page_break()

    # ---------- SUMARIO (manual) ----------
    heading(doc, "Sumario", level=1)
    for item in [
        "1. Especificacao do projeto",
        "2. Diagrama de classes",
        "3. Protótipos de tela",
        "4. Tabela de endpoints",
        "5. Diagrama Entidade-Relacionamento (DER)",
        "6. Instrucoes para executar o projeto",
    ]:
        doc.add_paragraph(item, style="List Bullet")
    doc.add_page_break()

    # ---------- 1. ESPECIFICACAO ----------
    heading(doc, "1. Especificacao do projeto", level=1)

    heading(doc, "1.1 Descricao da solucao", level=2)
    doc.add_paragraph(
        "O Woman's Shield e uma central de apoio para mulheres em situacao de violencia "
        "domestica na regiao de Sao Paulo e Osasco, inclusive para quem ainda nao tem medida "
        "protetiva. A funcionalidade central e o botao \"Pedir ajuda\": a usuaria cadastra ate "
        "6 contatos de confianca e, ao pedir ajuda, todos sao avisados, do que chega mais rapido "
        "ate ela ao mais lento. Contatos fora do horario em que costumam estar disponiveis "
        "tambem recebem o aviso, mas aparecem marcados como \"pode nao estar disponivel\"."
    )
    doc.add_paragraph(
        "Para a entrega de Domain Driven Design - Java, o recorte implementado e: Usuarias, "
        "Contatos de confianca (com disponibilidade semanal) e Alertas (com os contatos "
        "avisados, em ordem). Essas tres capacidades tem CRUD completo (GET, POST, PUT, DELETE "
        "quando aplicavel), regras de negocio reais na camada BO e tratamento de erros na "
        "camada de excecoes customizadas."
    )

    heading(doc, "1.2 Quem e o cidadao beneficiado", level=2)
    doc.add_paragraph(
        "Mulheres em situacao de violencia domestica nos municipios de Sao Paulo e Osasco "
        "(incluindo Carapicuiba, Barueri e Taboao da Serra), com destaque para quem ainda nao "
        "possui medida protetiva e precisa de uma rede de apoio imediata."
    )

    heading(doc, "1.3 Problema, em uma frase", level=2)
    doc.add_paragraph(
        "Em uma situacao de risco, a mulher muitas vezes nao tem tempo nem condicoes de "
        "avisar, um por um, os seus contatos de confianca, nem de saber quem vai conseguir "
        "chegar mais rapido ate ela."
    )

    heading(doc, "1.4 Como a solucao funciona", level=2)
    doc.add_paragraph(
        "A usuaria cadastra seus contatos de confianca (nome, telefone, bairro onde costumam "
        "estar e disponibilidade por dia/horario). Ao pedir ajuda, a API recebe o bairro atual "
        "e o tempo estimado (em minutos) de cada contato ate ela - calculado pelo motor de "
        "rotas do projeto (algoritmo de menor caminho, em Python) - e devolve a lista de "
        "contatos avisados, ordenada do mais rapido ao mais lento, indicando quem esta dentro "
        "do horario de disponibilidade e quem pode nao estar."
    )

    heading(doc, "1.5 Quais tecnologias foram empregadas", level=2)
    doc.add_paragraph(
        "Java 17 e Spring Boot (API RESTful), JDBC puro com PreparedStatement (padrao DAO), "
        "Oracle Database (servidor da FIAP), Maven (via Maven Wrapper) e deploy em nuvem no "
        "Render."
    )

    heading(doc, "1.6 Quem participa do ecossistema", level=2)
    doc.add_paragraph(
        "A usuaria (mulher em situacao de violencia), os contatos de confianca dela (familia, "
        "amigos, vizinhos), e, nas demais disciplinas do projeto integrado, a Delegacia da "
        "Mulher, Centros de Referencia, casas de acolhimento e a patrulha de ronda preventiva "
        "(fora do escopo desta API, mas parte do mesmo banco de dados)."
    )

    heading(doc, "1.7 Como o impacto pode ser medido e ampliado", level=2)
    doc.add_paragraph(
        "Numero de alertas registrados, tempo medio ate o primeiro contato avisado responder, "
        "proporcao de contatos disponiveis no horario do alerta e quantidade de usuarias ativas "
        "com pelo menos um contato cadastrado. A solucao pode ampliar integrando com servicos "
        "publicos (Delegacia da Mulher, SAMU) e expandindo a cobertura de bairros."
    )

    heading(doc, "1.8 Objetivos do modulo Java", level=2)
    for item in [
        "Implementar o dominio de Contatos de Confianca e Alertas em camadas (Model, DAO, BO, "
        "Controller, Excecoes), seguindo os padroes ensinados em aula.",
        "Persistir os dados no Oracle da FIAP via JDBC, com Singleton (ConnectionFactory) e "
        "Factory (DaoFactory).",
        "Expor uma API RESTful completa (15 endpoints) que a central web e os demais modulos "
        "do projeto integrado podem consumir.",
        "Aplicar as regras de negocio reais do dominio (validacao/mascara de telefone, limite "
        "de 6 contatos, exatamente 1 contato principal, disponibilidade por dia/hora, "
        "ordenacao do alerta por tempo estimado).",
    ]:
        doc.add_paragraph(item, style="List Bullet")

    heading(doc, "1.9 ODS conectados", level=2)
    doc.add_paragraph(
        "ODS 3 - Saude e bem-estar: a solucao busca reduzir danos a saude fisica e psicologica "
        "decorrentes da violencia domestica, agilizando o pedido de ajuda."
    )
    doc.add_paragraph(
        "ODS 10 - Reducao das desigualdades: mulheres em situacao de violencia domestica estao "
        "entre os grupos mais vulneraveis nas cidades atendidas; a central amplia o acesso "
        "delas a uma rede de apoio imediata."
    )
    doc.add_paragraph(
        "ODS 11 - Cidades e comunidades sustentaveis: contribui para tornar o espaco urbano "
        "mais seguro, usando dados de risco por bairro e por periodo do dia."
    )
    nota = doc.add_paragraph(
        "O projeto tambem dialoga diretamente com o ODS 5 (Igualdade de genero), ainda que "
        "esse ODS nao conste na lista sugerida no briefing da Global Solution."
    )
    nota.runs[0].italic = True

    doc.add_page_break()

    # ---------- 2. DIAGRAMA DE CLASSES ----------
    heading(doc, "2. Diagrama de classes", level=1)
    doc.add_paragraph(
        "O diagrama abaixo reflete o codigo Java implementado: o dominio (Usuaria, "
        "ContatoConfianca, Disponibilidade, Alerta, AlertaContato, Bairro e PeriodoDia) e, "
        "como exemplo representativo do padrao de camadas aplicado a todo o projeto, a cadeia "
        "Controller -> BO -> DAO (interface) -> DAO (implementacao) para Contato de Confianca, "
        "alem do Singleton (ConnectionFactory) e da Factory (DaoFactory)."
    )
    add_image_centered(doc, DIAGRAMA_CLASSES_IMG, width_cm=17)
    doc.add_page_break()

    # ---------- 3. PROTOTIPOS DE TELA ----------
    heading(doc, "3. Protótipos de tela", level=1)
    doc.add_paragraph(
        "As telas abaixo sao da central web do Woman's Shield (mesmo projeto, mesmo banco de "
        "dados) e ilustram, na pratica, o que os endpoints desta API sustentam."
    )

    heading(doc, "3.1 Lista de contatos de confianca", level=2)
    doc.add_paragraph(
        "Tela que consome GET /usuarias/{id}/contatos. O contato principal aparece primeiro; "
        "cada card mostra telefone mascarado, bairro e disponibilidade - os mesmos dados "
        "devolvidos pelo ContatoResponse da API."
    )
    add_image_centered(doc, os.path.join(TELAS, "02_lista_de_contatos.png"), width_cm=16)

    heading(doc, "3.2 Validacao do formulario de cadastro", level=2)
    doc.add_paragraph(
        "Formulario que consome POST /usuarias/{id}/contatos. Os erros destacados em vermelho "
        "(telefone invalido, bairro nao escolhido, nenhum dia de disponibilidade marcado) "
        "correspondem as mensagens que o CampoInvalidoException devolve com status 400."
    )
    add_image_centered(doc, os.path.join(TELAS, "03_validacao_do_formulario.png"), width_cm=16)

    heading(doc, "3.3 Alerta enviado", level=2)
    doc.add_paragraph(
        "Tela exibida apos POST /alertas. A ordem dos contatos (mais rapido ao mais lento) e o "
        "aviso \"pode nao estar disponivel\" refletem exatamente o que a AlertaBO calcula "
        "(ordenacao por minutosEstimados e flag disponivel por contato)."
    )
    add_image_centered(doc, os.path.join(TELAS, "01_alerta_enviado.png"), width_cm=16)

    doc.add_page_break()

    # ---------- 4. TABELA DE ENDPOINTS ----------
    heading(doc, "4. Tabela de endpoints", level=1)
    add_table(doc, ["Metodo", "Rota", "O que faz", "Sucesso"], ENDPOINTS)

    doc.add_paragraph()
    heading(doc, "4.1 Formato de erro padronizado", level=2)
    doc.add_paragraph(
        'Toda falha de validacao, conflito ou regra de negocio devolve o mesmo formato: '
        '{"status": 400, "erro": "Confira os campos destacados.", "campos": '
        '{"telefone": "DDD 00 nao existe."}}'
    ).runs[0].font.name = "Consolas"
    add_table(doc, ["Situacao", "Status HTTP"], ERROS)

    doc.add_page_break()

    # ---------- 5. DER ----------
    heading(doc, "5. Diagrama Entidade-Relacionamento (DER)", level=1)
    doc.add_paragraph(
        "Modelo logico completo do banco Oracle do Woman's Shield (15 tabelas, prefixo "
        "T_WS_). A API Java desta entrega utiliza as tabelas T_WS_USUARIA, "
        "T_WS_CONTATO_CONFIANCA, T_WS_DISPONIB_CONTATO, T_WS_ALERTA, T_WS_ALERTA_CONTATO, "
        "T_WS_BAIRRO e T_WS_PERIODO_DIA; as demais (pontos de apoio, rondas, casos "
        "acompanhados) pertencem a outras disciplinas do mesmo projeto integrado."
    )
    add_image_centered(doc, DER_IMG, width_cm=17)

    doc.add_page_break()

    # ---------- 6. INSTRUCOES DE EXECUCAO ----------
    heading(doc, "6. Instrucoes para executar o projeto", level=1)

    heading(doc, "6.1 Ferramentas utilizadas", level=2)
    for item in [
        "IntelliJ IDEA (ou qualquer IDE Java)",
        "JDK 17 ou superior",
        "Maven Wrapper (incluso no projeto - nao precisa instalar Maven)",
        "Oracle Database - servidor da FIAP (oracle.fiap.com.br:1521, SID ORCL)",
    ]:
        doc.add_paragraph(item, style="List Bullet")

    heading(doc, "6.2 Links", level=2)
    doc.add_paragraph(f"Repositorio no GitHub: {GITHUB_URL}")
    doc.add_paragraph(f"Link do deploy (Render): {DEPLOY_URL}")

    heading(doc, "6.3 Passo a passo", level=2)
    passos = [
        f"Clonar o repositorio: git clone {GITHUB_URL}.git",
        "Entrar na pasta do projeto: cd GSJava/womanshield-api (ajuste conforme o nome da pasta clonada).",
        "No Oracle da FIAP, executar na ordem os scripts de sql/01_DDL_womans_shield.sql, "
        "sql/02_DML_womans_shield.sql e sql/03_SEQUENCES_para_a_API_Java.sql (pacote de apoio "
        "do grupo).",
        "Copiar src/main/resources/db.properties.example para "
        "src/main/resources/db.properties e preencher db.url, db.user e db.password com as "
        "credenciais do Oracle da FIAP (esse arquivo fica fora do Git).",
        "Compilar e rodar: no Windows, .\\mvnw.cmd spring-boot:run (ou mvnw spring-boot:run no "
        "Linux/Mac). A API sobe em http://localhost:8080.",
        "Testar os endpoints com curl, Postman ou Insomnia, conforme a tabela da secao 4.",
    ]
    for i, passo in enumerate(passos, start=1):
        doc.add_paragraph(f"{i}. {passo}")

    heading(doc, "6.4 Variaveis de ambiente (producao / Render)", level=2)
    doc.add_paragraph(
        "Em vez de editar db.properties em producao, configure no dashboard do Render as "
        "variaveis WS_DB_URL, WS_DB_USER e WS_DB_PASSWORD - elas sobrescrevem os valores do "
        "arquivo (ver ConnectionFactory). O Render tambem injeta a variavel PORT "
        "automaticamente, ja usada em application.properties (server.port=${PORT:8080})."
    )

    out_path = os.path.join(BASE, "DOCUMENTACAO_WomansShield_DDD_Java.docx")
    doc.save(out_path)
    print("Gerado:", out_path)


if __name__ == "__main__":
    main()
