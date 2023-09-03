# gitlet Design Doc

## 0. Helpful instructions

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

## 1. Fundamental Data Structure

Additional referencing materials:

- [Git基本原理介绍(1)——代码仓库的初始化](https://www.bilibili.com/video/BV1TA411q75f): The video series which describe the working mechanism of the git based on book pro-git.
- **Gitlet intro playlist** given by https://sp21.datastructur.es/materials/proj/proj2/proj2#a-note-on-this-spec

### 1.1 Blob and its implementation strategy

TODO

### 1.2 Tree and its implementation strategy

TODO

### 1.3 Commit and its implementation strategy

TODO