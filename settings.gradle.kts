rootProject.name = "kotlin-multi-module-project"

include("default-core", "default-api", "default-consumer", "default-producer", "default-batch")

include("default-gateway")
include("hexagonal-payment")
include("ex-kotlin", "ex-module")
