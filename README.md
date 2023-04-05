# postRepoList

## Funcionamento e escolhas

Como usar: Para rodar o app é recomendado adicionar uma chave do github no arquivo ModuleInjector. A API do github é extremamente limitada em número de chamadas
mesmo para requisicoes autenticadas, e ainda mais para requisicoes nao autenticadas. Uma requisicao nao autenticada pode implicar em possíveis
maus desempenhos da aplicacao. Após isso, basta rodar a aplicacao. O mecanismo extre de favoritos implementado foi explicado em outra sessao.

Escolhas: Foi selecionada a arquitetura MVVM, combinada com alguns conceitos de Clean. A decisao foi tomada pensando em conceitos como escalabilidade,
separacao de propósitos de cada camada/classe, testabilidade e também em tirar o maior proveito possível de programacao reativa e dos componentes
de arquitetura do Android. Para o recyclewView Adapter, foi selecionada a abordagem com a utilizacao de um DiffUtils por ser uma das mais eficientes
Na ultima seção , há uma lista de possíveis melhorias e coisas que teriam sido feitas sem a limitacao de tempo

## Requisitos obrigatórios cumpridos

Chamada da API: Completada. Feito com retrofit e Okhttp. Por conta da limitação de chamadas muito baixa presente na API do github, foi inserida uma limitação adicional
no request onde cada página pudesse carregar somente 10 resultados de cada vez. Essa limitação foi percebida apenas no final da avaliação. Se tivesse sido
percebida a tempo, uma abordagem utilizando GraphQL teria sido preferida no lugar do retrofit, pois seria possível realizar uma chamada mais complexa,
utilizando o poder de processamento do servidor da API, ao invés da nossa camada de aplicacao. 

Exibir nome do repo, quantidade de estrelas, quantidade de fork, foto e nome do autor: Completada

Scroll infinito: Completado. 

Testes unitários: Foram feitos testes completos para o viewModel da atividade principal. Dada a arquitetura da solução proposta, o ideal seria que os testes
unitários tivessem sido concentrados na camada de Domain (use cases ou managers) , que concentra a lógica de negócios mais pesada do nosso código 
(o viewmodel contem apenas uma lógica leve). O teste foi feito no viewModel, considerando que a presente soluçao tem caráter avaliativo e com tempo
limitado.


## Requisitos opcionais 

Kotlin: O projeto foi feito 100% em kotlin

Android Architecture Components: O projeto foi feito usando alguns dos principais componentes de arquitetura do Jetpack, como o ViewModel, e o StateFlow. 

Rx ou Coroutines: O projeto foi feito de maneira 100% reativa, utilizando Coroutines com StateFlow.

Teste de UI com expresso: Nao realizado. 

Cache de imagens e da API: Foi colocado um cache local utilizando o RoomData base, permitindo que o aplicativo opere Offline. Foi também utilizado um 
caching de imagens, através da biblioteca Glide. O Cache da API nao foi utilizado para carregar as postagens do git mais rapidamente, pois a emissao 
do cache cancelaria o estado de "loading" da tela. Seria possível resolver esse problema, aprimorando o controle de estado, e fazendo o loading ser 
acionado apenas quando dados da API estiverem em processo de fetching (nao cancelando o estado de loading quando o cache fizer uma emissao). A solucao
proposta nao foi realizada porque o problema foi percebido no final do meu tempo. 

A arquitetura MVVM foi escolhida, utilizando-se do componente de arquitetura viewModel, que sobrevive a mudancas de orientacao, portanto é possível
aplicar mudancas de orientacao (como virar o celular para landscape) sem perder os dados presentes no viewModel (já carregados)

## Requisitos adicionais

Foram implementados também alguns requisitos adicionais. Há um mecanismo de favorito na aplicacao, é possível clicar no icone de estrela para 
favoritar um repositório. Como nao era possível fazer um post ou um put na API, esses favoritos sao salvos localmente, e quando uma lista de 
de repositorios é recuperada da API, há uma vericacao, e se os posts existentes no cache existem e estao favoritados em cache, esse estado é considerado. 
Tambem é possível eliminar a visualizacao de um repositório, segurando por alguns segundos (apenas visual, nada é feito no cache ou na API)

## Melhorias e cenários nao ideais da aplicacao

Algumas strings da aplicacao e alguns outros campos (como parametros do request e url da api) estao hardcoded. Com o devido tempo, o ideal seria move-los
para os parametros de entrada dos métodos, variaveis de Build e tambem para arquivos de resource. Os conceitos de Clean e MVVM utilizados, poderiam
ser melhorados ainda mais, com uma separacao ainda maior das camadas. Há também alguns erros básicos no layout, que poderiam ter sido melhorados
(como por exemplo carácteres desalinhados ou escapando da tela, que foram percebidos tardiamente). A nomenclatura e organizacao dos métodos poderia ser
melhorada, principalmente dentro da camada de Domain (manager files)e Model

