# AI Agent Cheat Sheet

Quick reference for using agents in Android Studio Terminal with Claude.

## 📋 Basic Usage

```bash
# 1. Show agent instructions
cat .github/agents/[agent-name].md

# 2. Copy output and paste in Claude
# 3. Ask your question
```

---

## 🎯 Available Agents

### 1. Code Reviewer
```bash
cat .github/agents/code-reviewer.md
```
**Then ask:** "Review [filename]"

**Best for:**
- PR reviews
- Code quality checks
- Best practices validation
- Security basics

---

### 2. Bug Hunter
```bash
cat .github/agents/bug-hunter.md
```
**Then ask:** "Scan [filename] for bugs"

**Best for:**
- Finding logic errors
- Edge case detection
- Concurrency issues
- Memory leaks

---

### 3. Test Generator
```bash
cat .github/agents/test-generator.md
```
**Then ask:** "Create tests for [filename]"

**Best for:**
- Unit tests
- Integration tests
- Test coverage
- Mock setup

---

### 4. Performance Analyzer
```bash
cat .github/agents/performance-analyzer.md
```
**Then ask:** "Analyze [filename] for performance issues"

**Best for:**
- Performance bottlenecks
- Memory optimization
- Recomposition issues
- Database optimization

---

### 5. Security Auditor
```bash
cat .github/agents/security-auditor.md
```
**Then ask:** "Audit [filename] for security issues"

**Best for:**
- Security vulnerabilities
- OWASP compliance
- Data exposure
- Input validation

---

### 6. Documentation Writer
```bash
cat .github/agents/documentation-writer.md
```
**Then ask:** "Add documentation to [filename]"

**Best for:**
- KDoc comments
- README files
- Architecture docs
- API documentation

---

### 7. Code Optimizer
```bash
cat .github/agents/code-optimizer.md
```
**Then ask:** "Optimize [filename]"

**Best for:**
- Code refactoring
- Performance improvements
- Code smells
- Architecture improvements

---

## 🚀 Quick Commands

### Review a file
```bash
cat .github/agents/code-reviewer.md
# "Review ProductViewModel.kt"
```

### Find bugs in directory
```bash
cat .github/agents/bug-hunter.md
# "Scan all files in src/commonMain/kotlin/viewmodel/"
```

### Generate tests
```bash
cat .github/agents/test-generator.md
# "Create comprehensive tests for UserRepository.kt"
```

### Performance check
```bash
cat .github/agents/performance-analyzer.md
# "Analyze ProductScreen.kt for performance issues"
```

### Security audit
```bash
cat .github/agents/security-auditor.md
# "Audit LoginScreen.kt for security vulnerabilities"
```

### Add docs
```bash
cat .github/agents/documentation-writer.md
# "Add KDoc to all public APIs in ProductRepository.kt"
```

### Optimize code
```bash
cat .github/agents/code-optimizer.md
# "Suggest optimizations for UserViewModel.kt"
```

---

## 💡 Pro Tips

### Tip 1: Review Multiple Files
```bash
cat .github/agents/code-reviewer.md
# "Review these files: ProductViewModel.kt, UserViewModel.kt, ProductRepository.kt"
```

### Tip 2: Focused Analysis
```bash
cat .github/agents/bug-hunter.md
# "Check ProductViewModel.kt specifically for:
# - Coroutine cancellation issues
# - State management bugs"
```

### Tip 3: Combine Agents
```bash
cat .github/agents/bug-hunter.md
# "Find bugs, then suggest fixes"

cat .github/agents/test-generator.md
# "Generate tests for those bugs"
```

### Tip 4: Shell Aliases (Optional)
Add to `~/.zshrc` or `~/.bashrc`:
```bash
alias agent-review='cat .github/agents/code-reviewer.md'
alias agent-bugs='cat .github/agents/bug-hunter.md'
alias agent-tests='cat .github/agents/test-generator.md'
alias agent-perf='cat .github/agents/performance-analyzer.md'
alias agent-security='cat .github/agents/security-auditor.md'
alias agent-docs='cat .github/agents/documentation-writer.md'
alias agent-optimize='cat .github/agents/code-optimizer.md'
```

Then: `source ~/.zshrc`

Now just type: `agent-review`

---

## 🎯 Common Workflows

### Before Committing
```bash
# 1. Review your changes
cat .github/agents/code-reviewer.md
# "Review all changed files"

# 2. Check for bugs
cat .github/agents/bug-hunter.md
# "Scan for potential issues"
```

### Before PR
```bash
# 1. Security check
cat .github/agents/security-auditor.md
# "Audit all changed files"

# 2. Performance check
cat .github/agents/performance-analyzer.md
# "Check for performance issues"

# 3. Add docs
cat .github/agents/documentation-writer.md
# "Document new features"
```

### Bug Fix Workflow
```bash
# 1. Find the bug
cat .github/agents/bug-hunter.md
# "Analyze [file] for [reported issue]"

# 2. Generate tests
cat .github/agents/test-generator.md
# "Create test that reproduces the bug"

# 3. Review fix
cat .github/agents/code-reviewer.md
# "Review my fix"
```

### New Feature
```bash
# 1. Code review
cat .github/agents/code-reviewer.md
# "Review the new feature"

# 2. Generate tests
cat .github/agents/test-generator.md
# "Create tests"

# 3. Add docs
cat .github/agents/documentation-writer.md
# "Document the feature"

# 4. Performance check
cat .github/agents/performance-analyzer.md
# "Check performance"
```

---

## 📝 Example Session

```bash
# Android Studio Terminal
$ cat .github/agents/code-reviewer.md

[Agent instructions appear...]

# Copy the output, then in Claude:
```

**In Claude:**
```
[Paste agent instructions]

Review ProductViewModel.kt and focus on:
1. Coroutine cancellation handling
2. StateFlow management
3. Error handling patterns
4. Memory leak prevention
```

**Claude Response:**
```
# Code Review: ProductViewModel.kt

## Issue #1: Flow Collection Blocks Loading State
Severity: High
Location: Lines 28-42
[detailed review...]
```

---

## 🆘 Troubleshooting

**Q: Agent instructions not showing**
**A:** Make sure you're in the project root directory:
```bash
cd /Users/neerajsoni/Documents/BrightlyCodeRepo/POC-KMPDatbase-ROOM/KMPDatabasePOC
pwd  # Verify location
```

**Q: File not found error**
**A:** Check the agents directory exists:
```bash
ls .github/agents/
```

**Q: Output is too long**
**A:** The agent files are large - this is normal. Copy all the text.

**Q: Claude doesn't follow the agent**
**A:** Make sure to paste the entire agent content first, then ask your question.

---

## 📚 Full Documentation

See [AGENT_USAGE_GUIDE.md](.github/AGENT_USAGE_GUIDE.md) for complete documentation.