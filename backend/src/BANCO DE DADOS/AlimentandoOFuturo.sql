
 BRUNO LIMA SANDES N°05

CAMILY BATISTA DE QUEIROZ  N°06

GUSTAVO BEZERRA CARVALHO N°16

KENNERT GABRIEL F. ANDRADE N°20

SAMARA MOURA DOS SANTOS N°32

ULISSES GABRIEL DA SILVA N°34









USE master;

GO
 

IF EXISTS (SELECT * FROM sys.databases WHERE name = 'AlimentandoOFuturo')

    DROP DATABASE AlimentandoOFuturo;

GO
 
CREATE DATABASE AlimentandoOFuturo;

GO
 
USE AlimentandoOFuturo;

GO
 


IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Usuario' AND xtype='U')

BEGIN

    CREATE TABLE Usuario (

        id BIGINT IDENTITY(1,1) PRIMARY KEY,

        nome NVARCHAR(100) NOT NULL,

        email NVARCHAR(100) NOT NULL UNIQUE,

        telefone NVARCHAR(20),

        senha NVARCHAR(255) NOT NULL,

        tipo_perfil NVARCHAR(20) DEFAULT 'USUARIO',

        pontos INT DEFAULT 0,

        nivel INT DEFAULT 1,

        data_cadastro DATETIME2 DEFAULT GETDATE(),

        data_ultimo_acesso DATETIME2,

        ativo BIT DEFAULT 1,

        endereco NVARCHAR(255),

        cidade NVARCHAR(100),

        estado NVARCHAR(50)

    );
 SELECT * FROM USUARIO
END

GO
 
-- =====================================================

-- TABELA: Horta

-- =====================================================

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Horta' AND xtype='U')

BEGIN

    CREATE TABLE Horta (

        id BIGINT IDENTITY(1,1) PRIMARY KEY,

        nome NVARCHAR(100) NOT NULL,

        descricao NVARCHAR(500),

        localizacao NVARCHAR(255) NOT NULL,

        latitude DECIMAL(10,8),

        longitude DECIMAL(11,8),

        tipo_cultivo NVARCHAR(20) NOT NULL,

        area_m2 DECIMAL(8,2),

        capacidade_pessoas INT,

        status NVARCHAR(20) DEFAULT 'PLANEJAMENTO',

        usuario_responsavel_id BIGINT NOT NULL,

        data_criacao DATETIME2 DEFAULT GETDATE(),

        data_ultima_atualizacao DATETIME2 DEFAULT GETDATE(),

        aprovada BIT DEFAULT 0,

        data_aprovacao DATETIME2,

        admin_aprovador_id BIGINT,

        motivo_rejeicao NVARCHAR(500),

        FOREIGN KEY (usuario_responsavel_id) REFERENCES Usuario(id),

        FOREIGN KEY (admin_aprovador_id) REFERENCES Usuario(id)

    );

  SELECT * FROM HORTA

END

GO
 
-- =====================================================

-- TABELA: Colheita

-- =====================================================

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='Colheita' AND xtype='U')

BEGIN

    CREATE TABLE Colheita (

        id BIGINT IDENTITY(1,1) PRIMARY KEY,

        horta_id BIGINT NOT NULL,

        usuario_id BIGINT NOT NULL,

        tipo_planta NVARCHAR(100) NOT NULL,

        quantidade_kg DECIMAL(8,2) NOT NULL,

        data_colheita DATE NOT NULL,

        data_registro DATETIME2 DEFAULT GETDATE(),

        qualidade NVARCHAR(20),

        destino NVARCHAR(255),

        observacoes NVARCHAR(500),

        foto_url NVARCHAR(500),

        FOREIGN KEY (horta_id) REFERENCES Horta(id),

        FOREIGN KEY (usuario_id) REFERENCES Usuario(id)

    );


  SELECT * FROM COLHEITA
END

GO
 
-- =====================================================

-- TABELA: password_reset_tokens

-- =====================================================

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='password_reset_tokens' AND xtype='U')

BEGIN

    CREATE TABLE password_reset_tokens (

        id BIGINT IDENTITY(1,1) PRIMARY KEY,

        token NVARCHAR(255) NOT NULL UNIQUE,

        email NVARCHAR(255) NOT NULL,

        expiry_date DATETIME2 NOT NULL,

        used BIT NOT NULL DEFAULT 0,

        created_date DATETIME2 DEFAULT GETDATE()

    );

END

GO
 
-- =====================================================

-- TABELA: support_requests

-- =====================================================

IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='support_requests' AND xtype='U')

BEGIN

    CREATE TABLE support_requests (

        id BIGINT IDENTITY(1,1) PRIMARY KEY,

        nome NVARCHAR(255) NOT NULL,

        email NVARCHAR(255) NOT NULL,

        assunto NVARCHAR(500) NOT NULL,

        mensagem NTEXT NOT NULL,

        usuario_id BIGINT,

        data_criacao DATETIME2 DEFAULT GETDATE(),

        status NVARCHAR(50) DEFAULT 'ABERTO',

        resposta NTEXT,

        data_resposta DATETIME2,

        FOREIGN KEY (usuario_id) REFERENCES Usuario(id)

    );

END

GO
 
-- =====================================================

-- ÍNDICES

-- =====================================================

CREATE INDEX IX_password_reset_tokens_email ON password_reset_tokens(email);

CREATE INDEX IX_password_reset_tokens_token ON password_reset_tokens(token);

CREATE INDEX IX_support_requests_email ON support_requests(email);

CREATE INDEX IX_support_requests_status ON support_requests(status);

CREATE INDEX IX_support_requests_data_criacao ON support_requests(data_criacao);

CREATE INDEX IX_support_requests_usuario_id ON support_requests(usuario_id);

GO
 
-- =====================================================

-- DADOS DE EXEMPLO

-- =====================================================
 
-- Usuário admin padrão

IF NOT EXISTS (SELECT * FROM Usuario WHERE email = 'admin@alimentandoofuturo.com')

BEGIN

    INSERT INTO Usuario (nome, email, senha, tipo_perfil, pontos, nivel)

    VALUES ('Administrador', 'admin@alimentandoofuturo.com', 

            '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 

            'ADMIN', 1000, 10);

END

GO
 
-- Usuários de exemplo

IF NOT EXISTS (SELECT * FROM Usuario WHERE email = 'maria@exemplo.com')

BEGIN

    INSERT INTO Usuario (nome, email, telefone, senha, pontos, nivel, cidade, estado) VALUES

    ('Maria Silva', 'maria@exemplo.com', '11999887766', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 150, 2, 'São Paulo', 'SP'),

    ('João Santos', 'joao@exemplo.com', '11888776655', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 200, 3, 'Rio de Janeiro', 'RJ'),

    ('Ana Costa', 'ana@exemplo.com', '11777665544', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 80, 1, 'Belo Horizonte', 'MG'),

    ('Carlos Oliveira', 'carlos@exemplo.com', '11666554433', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 300, 4, 'Curitiba', 'PR'),

    ('Fernanda Lima', 'fernanda@exemplo.com', '11555443322', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 120, 2, 'Porto Alegre', 'RS'),

    ('Roberto Mendes', 'roberto@exemplo.com', '11444332211', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 250, 3, 'Salvador', 'BA');

END

GO
 
-- Hortas

DECLARE @userId1 BIGINT = (SELECT id FROM Usuario WHERE email = 'maria@exemplo.com');

DECLARE @userId2 BIGINT = (SELECT id FROM Usuario WHERE email = 'joao@exemplo.com');

DECLARE @userId3 BIGINT = (SELECT id FROM Usuario WHERE email = 'carlos@exemplo.com');

DECLARE @userId4 BIGINT = (SELECT id FROM Usuario WHERE email = 'fernanda@exemplo.com');

DECLARE @userId5 BIGINT = (SELECT id FROM Usuario WHERE email = 'roberto@exemplo.com');

DECLARE @adminId BIGINT = (SELECT id FROM Usuario WHERE email = 'admin@alimentandoofuturo.com');
 
INSERT INTO Horta (nome, descricao, localizacao, latitude, longitude, tipo_cultivo, area_m2, capacidade_pessoas, status, usuario_responsavel_id, aprovada, admin_aprovador_id, data_aprovacao) VALUES

('Horta Comunitária Vila Verde', 'Horta urbana focada em hortaliças orgânicas', 'Rua das Flores, 123 - Vila Verde, SP', -23.550520, -46.633308, 'ORGANICO', 150.50, 20, 'CRESCIMENTO', @userId1, 1, @adminId, GETDATE()),

('Horta Escolar Esperança', 'Projeto educativo em escola pública', 'Curitiba/PR', -25.441105, -49.276855, 'ORGANICO', 120.00, 30, 'CRESCIMENTO', @userId3, 1, @adminId, GETDATE()),

('Jardim Medicinal Comunitário', 'Cultivo de plantas medicinais', 'Porto Alegre/RS', -30.034647, -51.217658, 'TRADICIONAL', 90.75, 12, 'PLANTIO', @userId4, 1, @adminId, GETDATE());

GO
 
-- Colheitas

DECLARE @hortaId1 BIGINT = (SELECT id FROM Horta WHERE nome = 'Horta Comunitária Vila Verde');

DECLARE @hortaId2 BIGINT = (SELECT id FROM Horta WHERE nome = 'Horta Escolar Esperança');

DECLARE @hortaId3 BIGINT = (SELECT id FROM Horta WHERE nome = 'Jardim Medicinal Comunitário');
 
INSERT INTO Colheita (horta_id, usuario_id, tipo_planta, quantidade_kg, data_colheita, qualidade, destino, observacoes) VALUES

(@hortaId1, @userId1, 'Alface', 5.5, '2025-01-10', 'EXCELENTE', 'Doação comunitária', 'Primeira colheita da temporada.'),

(@hortaId2, @userId3, 'Rúcula', 2.8, '2025-02-01', 'BOA', 'Merenda escolar', 'Colheita feita pelos alunos.'),

(@hortaId3, @userId4, 'Hortelã', 1.2, '2025-02-03', 'EXCELENTE', 'Chás comunitários', 'Aroma intenso e folhas saudáveis.');

GO
 
PRINT '✅ Banco AlimentandoOFuturo criado e populado com sucesso!';

 