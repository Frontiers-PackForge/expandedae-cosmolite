---
navigation:
  parent: expandedae-index.md
  title: Porta E/S Expandida
  icon: exp_io_port
  position: 1
categories:
  - expandedae
item_ids:
- expandedae:exp_io_port
---

<GameScene zoom="4" background="transparent">
  <ImportStructure src="structures/exp_io_port.snbt" />
  <IsometricCamera yaw="195" pitch="30" />
</GameScene>

Uma Porta de E/S ainda melhor que permite transferir grandes quantidades de qualquer coisa muito rapidamente.
Pode ser útil para produzir muitas singularidades no <ItemLink id="ae2:condenser" />.
Confira a tabela abaixo para os valores exatos!"

| Qtd. de Melhorias | Vel. de Transferência | Vel. de Transferência (legível) |
|-------------------|-----------------------|---------------------------------|
| 0                 | 4,194,303             | MAX_INT / 512                   |
| 1                 | 8,388,606             | MAX_INT / 256                   |
| 2                 | 33,554,424            | MAX_INT / 64                    |
| 3                 | 134,217,696           | MAX_INT / 16                    |
| 4                 | 536,870,784           | MAX_INT / 4                     |
| 5                 | 2,147,483,136         | MAX_INT                         |

_Nota: Para Java, Integer.MAX_VALUE, neste caso chamado MAX_INT, é 2.147.483.647._