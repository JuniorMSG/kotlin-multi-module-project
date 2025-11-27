rootProject.name = "kotlin-multi-module-project"

include("default-core", "default-api", "default-consumer", "default-producer", "default-batch", "exercise")
include("hexagonal-payment")
