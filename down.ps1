docker compose down
docker rmi $(docker images --filter reference=bsuir-hrm-dataanlyzer-* -q)
