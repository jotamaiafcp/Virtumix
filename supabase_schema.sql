-- SQL para criar tabela de receitas no Supabase
-- Executa isto no SQL Editor do teu projeto Supabase

-- Tabela de receitas
CREATE TABLE IF NOT EXISTS recipes (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    name TEXT NOT NULL,
    description TEXT,
    image_url TEXT,
    tags TEXT[] DEFAULT '{}',
    prepare_time INTEGER,
    cook_time INTEGER,
    servings INTEGER,
    ingredients TEXT[] DEFAULT '{}',
    steps TEXT[] DEFAULT '{}',
    user_id TEXT,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    updated_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

-- Índices para performance
CREATE INDEX IF NOT EXISTS idx_recipes_user_id ON recipes(user_id);
CREATE INDEX IF NOT EXISTS idx_recipes_created_at ON recipes(created_at DESC);
CREATE INDEX IF NOT EXISTS idx_recipes_name ON recipes USING gin(to_tsvector('portuguese', name));

-- Tabela de favoritos
CREATE TABLE IF NOT EXISTS favorites (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id TEXT NOT NULL,
    recipe_id TEXT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, recipe_id)
);

CREATE INDEX IF NOT EXISTS idx_favorites_user_id ON favorites(user_id);

-- Tabela de lista de compras
CREATE TABLE IF NOT EXISTS shopping_items (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id TEXT NOT NULL,
    name TEXT NOT NULL,
    quantity TEXT,
    checked BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_shopping_user_id ON shopping_items(user_id);

-- Tabela de planeador semanal
CREATE TABLE IF NOT EXISTS weekly_plans (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id TEXT NOT NULL,
    day_of_week INTEGER NOT NULL CHECK (day_of_week BETWEEN 1 AND 7),
    recipe_id TEXT NOT NULL,
    week_start DATE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT NOW(),
    UNIQUE(user_id, day_of_week, week_start)
);

CREATE INDEX IF NOT EXISTS idx_weekly_plans_user_week ON weekly_plans(user_id, week_start);

-- Habilita Row Level Security (RLS)
ALTER TABLE recipes ENABLE ROW LEVEL SECURITY;
ALTER TABLE favorites ENABLE ROW LEVEL SECURITY;
ALTER TABLE shopping_items ENABLE ROW LEVEL SECURITY;
ALTER TABLE weekly_plans ENABLE ROW LEVEL SECURITY;

-- Políticas RLS (ajusta conforme necessário)
-- Permite leitura pública de receitas
CREATE POLICY "Receitas públicas legíveis" ON recipes
    FOR SELECT USING (true);

-- Permite utilizadores criar/editar as suas próprias receitas
CREATE POLICY "Utilizadores gerem as suas receitas" ON recipes
    FOR ALL USING (auth.uid()::text = user_id);

-- Favoritos apenas visíveis/editáveis pelo próprio utilizador
CREATE POLICY "Utilizador gere os seus favoritos" ON favorites
    FOR ALL USING (auth.uid()::text = user_id);

-- Lista de compras apenas visível/editável pelo próprio utilizador
CREATE POLICY "Utilizador gere a sua lista de compras" ON shopping_items
    FOR ALL USING (auth.uid()::text = user_id);

-- Planeador semanal apenas visível/editável pelo próprio utilizador
CREATE POLICY "Utilizador gere o seu planeador" ON weekly_plans
    FOR ALL USING (auth.uid()::text = user_id);
