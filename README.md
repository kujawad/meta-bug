# meta-bug
[![Build Status](https://travis-ci.org/l14D35/meta-bug.svg?branch=master)](https://travis-ci.org/l14D35/meta-bug)

## Steps to keep the code tidy
### Maintain the code convention
1. Use **English** language.
2. Use [camelCase](https://pl.wikipedia.org/wiki/CamelCase) in methods and fields naming:
- :heavy_check_mark: do: `thisIsSomeMethod()`, `private int someInteger`
- :x: don't do: `this_is_some_method()`, `private int some_integer`
3. Write tests whenever you touch any logic
### Git
1. Work on forks.
2. Work on branches, the `master` branch **should not** be changed in any way.
3. Create logic commit messages:
- :heavy_check_mark: do: "Added exception handler for client querying"
- :x: don't do: "Added new feature"
4. Create pull requests from your fork's branch to `upstream/master`, there your code will go through
"Code review".
5. **Every time when you are willing to write some code, fetch changes with upstream**

## Syncing a fork
Sync a fork of a repository to keep it up-to-date with the upstream repository.
First you have to configure a remote for a fork:
1. List the current configured remote repository for your fork.
```bash
$ git remote -v
> origin  https://github.com/YOUR_USERNAME/YOUR_FORK.git (fetch)
> origin  https://github.com/YOUR_USERNAME/YOUR_FORK.git (push)
```
2. Specify a new remote upstream repository that will be synced with the fork.
```bash
$ git remote add upstream https://github.com/ORIGINAL_OWNER/ORIGINAL_REPOSITORY.git
```
3. Verify the new upstream repository you've specified for your fork.
```bash
$ git remote -v
> origin    https://github.com/YOUR_USERNAME/YOUR_FORK.git (fetch)
> origin    https://github.com/YOUR_USERNAME/YOUR_FORK.git (push)
> upstream  https://github.com/ORIGINAL_OWNER/ORIGINAL_REPOSITORY.git (fetch)
> upstream  https://github.com/ORIGINAL_OWNER/ORIGINAL_REPOSITORY.git (push)
```
4. Fetch the branches and their respective commits from the upstream repository. Commits to `master` will be stored in a local branch, `upstream/master`.
```bash
$ git fetch upstream
> remote: Counting objects: 75, done.
> remote: Compressing objects: 100% (53/53), done.
> remote: Total 62 (delta 27), reused 44 (delta 9)
> Unpacking objects: 100% (62/62), done.
> From https://github.com/ORIGINAL_OWNER/ORIGINAL_REPOSITORY>  * [new branch]      master     -> upstream/master
```
5. Check out your fork's local `master` branch.
```bash
$ git checkout master
> Switched to branch 'master'
```
6. Merge the changes from `upstream/master` into your local `master` branch. This brings your fork's `master` branch into sync with the upstream repository, without losing your local changes.
```bash
$ git merge upstream/master
> Updating a422352..5fdff0f
> Fast-forward
>  README                    |    9 -------
>  README.md                 |    7 ++++++
>  2 files changed, 7 insertions(+), 9 deletions(-)
>  delete mode 100644 README
>  create mode 100644 README.md
```
