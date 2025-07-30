Para compilar, abra um terminal nessa pasta e faça:
    javac -d . $(find . -name "*.java")
Para executar, faça:
    java compiler.exe

OBS: O teste que estiver na pasta testes roda automaticamente. Não pode ter mais de um teste
na pasta, pois a tabela de simbolos não é reiniciada entre os testes, então variaveis do
primeiro teste, passam para o segundo, o que gera erro.