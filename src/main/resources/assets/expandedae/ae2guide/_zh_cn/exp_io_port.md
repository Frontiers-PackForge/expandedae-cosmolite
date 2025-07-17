---
navigation:
  parent: expandedae-index.md
  title: 拓充IO端口
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

更快的[ME IO 端口](ae2:items-blocks-machines/io_port)，更快的合成速度。
在 <ItemLink id="ae2:condenser" /> 中合成奇点*非常有用*。
具体速率如下表：

| 升级数量 | 传输速度       | 相当于        |
|----------|----------------|---------------|
| 0        | 4,194,303      | MAX_INT / 512 |
| 1        | 8,388,606      | MAX_INT / 256 |
| 2        | 33,554,424     | MAX_INT / 64  |
| 3        | 134,217,696    | MAX_INT / 16  |
| 4        | 536,870,784    | MAX_INT / 4   |
| 5        | 2,147,483,136  | MAX_INT       |

*注：对于Java，Int（整形）的上限（即`MAX_INT`）为2,147,483,647*