Projeto desenvolvido com o intuíto de migração para o cargo de desenvolvedor Java na empresa NovaXS - Excelência em Sistemas.

Principais Tecnologias utilizada no teste:
Java: JDK 1.8;
Container: Jersey 3.1.5;
Banco de dados: PostgreSQL 15;

Rotas API:
Método POST "pokemon", insere um novo pokemon com os dados do pokemon informados no body da requisição;
Método GET "pokemon/NUMERO_DO_POKEMON" retorna os dados do pokemon com o número passado, campo "num" no arquivo pokedex.json, em formato JSON;
Método PUT "pokemon/NUMERO_DO_POKEMON" atualiza o pokemon com o numero passado usando os dados do pokemon informados no body da requisição;
Metodo DELETE "pokemon/NUMERO_DO_POKEMON" exclui o pokemon que possui o numero informado;
Metodo GET "pokemons": Retorna todos os pokemons;

Rotas de Busca: 
Metodo GET "pokemons/PAGINA/QUANTIDADE": Divide os pokemons em páginas pela quantidade informada e retorna a pagina informada. Exemplo: GET "http://urldoservidor/pokemons/2/50", irá retornar do pokemon 51 ao 100;
Metodo GET "pokemons/TIPO_DO_POKEMON": Retorna os pokemons do tipo (campo 'type' no json);
