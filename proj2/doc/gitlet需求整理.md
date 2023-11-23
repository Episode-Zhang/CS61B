# gitlet需求整理

## 1. 整体目标

project 2需要实现一个玩具版的git版本控制系统，称为gitlet，这个迷你版本控制系统需要支持：

1. 保存整个目录中所有文件的内容(也可以是单个文件)，我们称之为*committing*，单次对目录和文件的快照称为*commit*；
2. 给出指定版本，可以恢复出该版本里某个或者多个文件的内容，我们称之为*checking out*；
3. 浏览所有历史快照记录，我们管这些历史记录叫做*logs*；
4. 维护相互关联的commit序列，我们管这种序列叫*branches*；
5. 将一个branch中记录的文件内容的变动合并到另一个branch中，我们称之为*merging*；

> 关键词：***commit***, ***check out***, ***log***, ***branch***, ***merge***

---

## 2. 错误处理

部分命令会给出对应的错误/异常行为发生时需要抛出的信息，还有一部分错误/异常信息不依赖于具体的命令，而是由用户的部分行为触发——这些异常信息在抛出的同时会中断程序当前操作，并且不修改系统状态。此外，系统实现的时候**仅需考虑指定的异常**，实现者不应该定义和实现任何其它的异常，以及程序的中断操作。

具体命令对应的错误处理我们会放到“具体命令的需求”这一节去展现，从整个系统的角度而言，实现者应该要考虑以下的错误：

1. 如果用户启动gitlet却没有给出任何参数，打印

   `Please enter a command.`

   并退出(`System.exit(0)`)；

2. 如果用户给出了一条不存在的命令，打印

   `No command with that name exists.`

   并退出(`System.exit(0)`)；

3. 如果用户输入的参数个数或者格式有误，打印

   `Incorrect operands.`

   并退出(`System.exit(0)`)；

4. 如果用户输入的命令依赖于gitlet的初始化，但是当前工作目录内不存在`.gitlet`目录，打印

   `Not in an initialized Gitlet directory.`

   并退出(`System.exit(0)`)；

同时，系统强制要求将argument[0]设置为`System.exit(0)`参数。

---

## 3. 具体命令

以下是gitlet需要支持的具体命令，它们中的绝大多数都对运行时的性能(以时间复杂度为衡量标准)有要求：

1. init

   - 命令：`java gitlet.Main init`
   - 职责：将当前目录**初始化**为一个gitlet工作目录，它会：
     - 创建一个`.gitlet`的文件夹；
     - 初始化一个commit，message为`initial commit`，时间戳为“00:00:00 UTC, Thursday, 1 January 1970”，这个commit作为所有commit的祖先；
     - 初始化一个branch，名字为`master`；
   - 运行时性能：对任意变量$x$的输入规模$N$（当我们在说“任意变量”时，表名这个变量可以是getlet系统里任意一个对象，例如追踪的文件数，当前branch中提交的commit数等等），都保持$\Theta(1)$常数复杂度
   - 失败用例：若当前目录已经初始化，则gitlet退出，同时打印信息`A Gitlet version-control system already exists in the current directory.`
   - 是否危险：否

2. add

   - 命令：`java gitlet.Main add [file name]`
   - 职责：将指定的文件写入**暂存区(staging area)**：
     - 若该文件已经存入暂存区，则add命令会将原有存入的内容用文件的最新内容覆写掉
     - 如果该文件的版本与当前工作目录的版本一致(意味着没有发生变动)，那么add命令不应该将该文件加入暂存区，如果该文件已在暂存区内，则删除暂存区中的该文件；
     - 如果该文件已从gitlet的追踪中删除(例如使用了`getlet rm`)命令，那么将该文件暂存区中的内容移除；
   - 运行时性能：令$N$为gitlet追踪的文件数，$M$为当前branch中的commit数，时间复杂度上界应为$O(N)$，$O(\lg{M})$；
   - 失败用例：如果add的文件不存在，则打印`File does not exist.`，同时退出
   - 是否危险：否

3. commit

   - 命令：`java gitlet.Main commit [message]`

   - 职责：为当前暂存区中的所有文件以及当前commit中所追踪的文件创建一个**快照**：

     - 默认情况下，一个commit追踪的文件快照应该和它的父commit保持一致；
     - commit不会修改祖辈commit的内容，它在继承祖辈的文件快照时，同时**仅**更新那些在暂存区中的文件所对应快照的内容；
     - commit会为那些祖辈commit中未曾记录，但是在暂存区内新增的文件生成对应的快照；
     - commit中追踪的文件可以被移除(gitlet rm)；

     关于commit命令的注意点有：

     - commit完成后，暂存区需要被清空；
     - commit不会影响工作区文件的内容；
     - 如果追踪的文件内容发生了改变但是没有被加入暂存区，那么commit不会改变该已追踪的文件的内容；
     - commit完成后，commit树中需要新增对应的commit结点；
     - commit命令必须传入message参数，用来描述本次commit，且message参数占据一个参数位，这意味着如果message中间存在空格，需要用引号引住；
     - 每个commit以它的sha1 hash码作为唯一标识id，且必须携带追踪的文件引用、父commit的引用、log message、commit时的时间戳；

   - 运行时性能：令$N$为当前分支中的commit数，$M$为所有被追踪的文件数，则commit命令的时间复杂度应为对$N$为$\Theta(1)$，对$M$为O(M)$；同时空间复杂度上，每一次commit增加的文件数不得多于本次暂存区中add进来的文件数量(意思是不要重复添加父commit结点已经维护了的文件)

   - 失败用例：

     - 如果暂存区中没有文件，则退出，并打印`No changes added to the commit.`；
     - 如果提交的message参数为空，则退出，并打印`Please enter a commit message.`；
     - 如果工作区内被追踪的文件缺失或者被改动。不会报错(commit仅关注`.gitlet`目录中的内容，换言之，这个错误应该在`commit`的前置环节被处理)；

   - 是否危险：否

4. rm
   - 命令：`java gitlet.Main rm [file name]`
   - 职责：将存入暂存区的文件移出暂存区或者将已追踪的文件取消追踪并从工作目录中删除
   - 运行时性能：对任意给定变量的规模均为常数复杂度$\Theta(1)$
   - 失败用例：若给出的文件名既不在暂存区，同时也处于未追踪的状态，则打印`No reason to remove the file.`
   - 是否危险：是
5. log
   - 命令
