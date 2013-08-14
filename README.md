taskListAndroid
===============

Personal task list using GPS

Como funciona?
=============

Primeiro o usuário cadastra uma Task com sua descrição e anotações.

Ex.:  Description: Super Mercado Extra.
      Notes: Comprar pão, banana.

Cada Task possui um atributo booleano de Alert, ou seja, caso seja marcado como true, o usuário quando chegar no local escolhido pelo usuário, o mesmo será alertado que ele possuiTasks a serem feitas naquele local.

Na criação da task é possível escolher se vai digitar um endereço para setar o Location ou pegar a localização atual do usuário. A App alerta quando ele chegar/sair do local ou na proximidade de 100m (cem metros).

Após finalizar o cadastro da Task, a App irá ficar utilizar o GeofenceService e ir verificando quando a localização do usuário mudar e chegar ou se aproximar 100m do endereço escolhido para a Task e dará Notifications para o usuário informando quais Tasks que ele tem que fazer naquele local.

Usuário tem a opção de marcar como Done cada Task realizada pelo mesmo, na tela de listagem, tela principal.
