# 结构设计

## 1. 领域划分

问题：gitlet需要做哪些事情，它能做哪些事情？

职责梳理：

- add文件时，会保存被add文件的一份快照；
- commit时，会把add过的所有文件的快照集中记录，并进行索引；
- branch会记录当前分支上的所有commit；
- checkout时，会把对应commit的对应文件恢复到工作区，如果checkout branch，那么会把整个branch中最新的内容全部恢复到工作区

程序设计：

- add文件时，所谓的快照就是，我需要通过**唯一**的标识(成为索引)，索引到add时刻被add的文件的**文件名**以及**内容**。其解决的关键问题为：
    - 文件是谁 - 文件名
    - 文件长啥样 - 文件内容
    - 怎么找到这个文件 - 索引
- commit时，这个“集中记录”要求我们需要知道，**上一次commit结束时**到**这一次commit开始时**，一共add了哪些文件，同时我们也需要给这些add的文件的诸如文件名、内容、索引一个索引。其解决的关键问题是：
    - 两次commit之间add了哪些文件 - buffer/暂存区/staged area
    - 如何记录和索引这些add的文件 - tree
    - 记录和索引哪些文件 - blob
- branch需要指导整个branch上的commit链，它的root可能是init时候来的，也有可能是别的branch里分出来的。其解决的关键问题是：
    - 如何记录和追溯整条branch链 - parent指针
    - 如何不和其它branch搞混 - branch指针指向当前branch的最新commit，commit记录和追溯自己的父commit

---

## 2. 简化与限制

1. 代码仓库中只有一个根目录与其余**非目录文件**(无文本嵌套)；
2. merge只会作用于两个分支；
3. metadata只涵盖时间辍与log信息；
4. `.gitlet`文件夹存放gitlet的元数据信息，若某个目录存在`.gitlet`文件夹，则该目录是一个仓库(repo)；
5. 预期的错误必须被`System.out.println`出来，且必须以**英文句号(.)**结尾；

---

## 3. Helpful instructions

1. Compile all java files in proj2's directory, using `javac getlet/*.java `;
2. Copy compiled java class files into {someplace}/{testfolder}/gitlet by using
3. If source code has changed in some way, redo step 2;
4. Test gitlet's behavior to see if it matches your expectation;

(btw, hit a lot of return in your terminal as many as you can, which makes you look like a pro🤪)

5. The simple structure diagram of the gitlet:

   ![image-20230903170113956](/home/jeffery-zhang/.config/Typora/typora-user-images/image-20230903170113956.png)

   Here's how `add-commit`workflow works:

    - At the beginning, you created a file named `Hello.txt` with its content:

      ```
      Hello
      ```

      By the time it was created and written, it still stayed at the "Working Directory" until you use `gitlet add` command.

    - When you hit `gitlet add Hello.txt` command in terminal and which has been parsed and executed by the java vm, it records content of `Hello.txt` into a blob(which is immutable in git repository), and created a structure representing added files and its corresponding blobs in "Staging area".

    - Finally, when you hit `gitlet commit {comment}`, the gitlet will take a snapshot of all contents in "Staging area" by creating a commit. A commit must contain its metadata, including commit message, unix timestamp and all file it tracks.

   ![image-20230903173818632](/home/jeffery-zhang/.config/Typora/typora-user-images/image-20230903173818632.png)

6. Java serialization is by default deep serialization, that means all nested references will be stored in current object. In order to avoid doing that(for the purpose of saving space and time, reduce redundancy), we can using hash code(as file name) instead of pointer to represent a file object. Actually it is what git itself does.

Additional referencing materials:

- [Git基本原理介绍(1)——代码仓库的初始化](https://www.bilibili.com/video/BV1TA411q75f): The video series which describe the working mechanism of the git based on book pro-git.
- **Gitlet intro playlist** given by https://sp21.datastructur.es/materials/proj/proj2/proj2#a-note-on-this-spec

## 4. 结构设计

