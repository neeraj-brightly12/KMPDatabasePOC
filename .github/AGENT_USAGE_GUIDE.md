# AI Agent Usage Guide

This guide shows how to use the specialized AI agents in `.github/agents/` with different AI tools.

---

## ⚡ Quick Start (Android Studio Terminal + Claude)

**Step-by-step:**
1. Open Terminal in Android Studio (bottom panel or `Alt+F12`)
2. Navigate to project root (if not already there)
3. Run agent command:
```bash
cat .github/agents/code-reviewer.md
```
4. Copy the displayed text
5. Open Claude (web/app/CLI)
6. Paste the agent text
7. Add your request: "Review ProductViewModel.kt"

**Example Session:**
```bash
# In Android Studio Terminal
cd /path/to/KMPDatabasePOC
cat .github/agents/code-reviewer.md
# [copy output]

# In Claude
# [paste agent text]
# Then type: "Review ProductViewModel.kt focusing on state management and coroutines"
```

**All Available Agents:**
```bash
cat .github/agents/code-reviewer.md      # Code review
cat .github/agents/bug-hunter.md         # Find bugs
cat .github/agents/test-generator.md     # Generate tests
cat .github/agents/performance-analyzer.md  # Performance issues
cat .github/agents/security-auditor.md   # Security audit
cat .github/agents/documentation-writer.md  # Add docs
cat .github/agents/code-optimizer.md     # Optimize code
```

---

## 🤖 GitHub Copilot (Recommended)

### In VS Code

#### Method 1: Direct Reference (Easiest)
1. Open Copilot Chat (`Ctrl+Shift+I` / `Cmd+Shift+I`)
2. Type:
```
@workspace Using .github/agents/code-reviewer.md, review ProductViewModel.kt
```

#### Method 2: File Reference
```
#file:.github/agents/code-reviewer.md
Review #file:src/.../ProductViewModel.kt
```

#### Method 3: Use Code Snippets
Type in chat:
- `agent-review` + Tab → Code review
- `agent-bugs` + Tab → Bug hunting
- `agent-tests` + Tab → Test generation
- `agent-perf` + Tab → Performance analysis
- `agent-security` + Tab → Security audit
- `agent-docs` + Tab → Documentation
- `agent-optimize` + Tab → Code optimization

### In GitHub Pull Requests

Comment on PR:
```markdown
@copilot Using the guidelines from `.github/agents/code-reviewer.md`,
please review this PR focusing on:
- Security issues
- Performance problems
- Best practices violations
```

### In GitHub Issues

```markdown
@copilot Using `.github/agents/bug-hunter.md`, analyze the reported issue
in UserViewModel.kt and suggest fixes.
```

---

## 🎯 Claude (CLI or Web)

### In Android Studio Terminal ⭐ RECOMMENDED

**Method 1: Two-Step Approach (Simplest)**
```bash
# Step 1: Display agent instructions
cat .github/agents/code-reviewer.md

# Step 2: Copy output, then tell Claude:
# "Review ProductViewModel.kt"
```

**Method 2: Direct Pipe**
```bash
# Load agent and ask in one command
cat .github/agents/code-reviewer.md | claude "Review ProductViewModel.kt"
```

**Method 3: Combined Command**
```bash
claude "$(cat .github/agents/code-reviewer.md)

Review ProductViewModel.kt focusing on coroutine safety and state management."
```

#### 🚀 Quick Commands for Android Studio Terminal

```bash
# 1. Code Review
cat .github/agents/code-reviewer.md
# Then ask Claude: "Review ProductViewModel.kt"

# 2. Find Bugs
cat .github/agents/bug-hunter.md
# Then ask: "Scan all ViewModels for bugs"

# 3. Generate Tests
cat .github/agents/test-generator.md
# Then ask: "Create tests for UserRepository"

# 4. Performance Analysis
cat .github/agents/performance-analyzer.md
# Then ask: "Analyze ProductScreen for performance issues"

# 5. Security Audit
cat .github/agents/security-auditor.md
# Then ask: "Audit authentication flow"

# 6. Add Documentation
cat .github/agents/documentation-writer.md
# Then ask: "Add KDoc to all public APIs in ProductRepository"

# 7. Optimize Code
cat .github/agents/code-optimizer.md
# Then ask: "Suggest optimizations for src/commonMain/kotlin/viewmodel/"
```

#### 💡 Pro Tips for Android Studio

**Tip 1: Review Multiple Files**
```bash
cat .github/agents/code-reviewer.md
# Then: "Review these files: ProductViewModel.kt, UserViewModel.kt"
```

**Tip 2: Focus on Specific Issues**
```bash
cat .github/agents/bug-hunter.md
# Then: "Check ProductViewModel.kt specifically for:
# - Coroutine cancellation issues
# - State management bugs
# - Memory leaks"
```

**Tip 3: Create Shell Aliases**
Add to your `~/.zshrc` or `~/.bashrc`:
```bash
# Quick agent access
alias agent-review='cat .github/agents/code-reviewer.md'
alias agent-bugs='cat .github/agents/bug-hunter.md'
alias agent-tests='cat .github/agents/test-generator.md'
alias agent-perf='cat .github/agents/performance-analyzer.md'
alias agent-security='cat .github/agents/security-auditor.md'
alias agent-docs='cat .github/agents/documentation-writer.md'
alias agent-optimize='cat .github/agents/code-optimizer.md'
```

Then reload: `source ~/.zshrc`

Now you can simply type:
```bash
agent-review
# Then ask Claude
```

**Tip 4: Navigate to Project Root**
```bash
# In Android Studio terminal, make sure you're in project root
cd /Users/neerajsoni/Documents/BrightlyCodeRepo/POC-KMPDatbase-ROOM/KMPDatabasePOC

# Then use agents
cat .github/agents/code-reviewer.md
```

### With Claude Code CLI
```bash
# If you have Claude Code CLI installed
claude-code "$(cat .github/agents/code-reviewer.md)

Review ProductViewModel.kt"
```

### With Claude Web
1. Copy agent content: `cat .github/agents/code-reviewer.md`
2. Paste in Claude chat
3. Add your request: "Now review ProductViewModel.kt"

### With Claude in IDE (Cursor/Windsurf)
If using Claude integration:
1. Select the agent file
2. Include it as context
3. Ask your question

---

## 💬 ChatGPT

### In ChatGPT Web/App
1. **Copy agent**: `cat .github/agents/code-reviewer.md`
2. **Paste** in chat
3. **Follow up** with: "Review this file: [paste code]"

### With ChatGPT Custom Instructions
1. Go to Settings → Custom Instructions
2. Paste agent content in "How would you like ChatGPT to respond?"
3. This applies to all conversations

---

## 🔧 Other AI Tools

### Cursor IDE
```
@agent Use .github/agents/code-reviewer.md as instructions
Review #ProductViewModel.kt
```

### Tabnine
1. Open AI Chat
2. Reference: "Follow guidelines in .github/agents/code-reviewer.md"
3. Ask: "Review ProductViewModel.kt"

### Codeium
Similar to Copilot - reference files:
```
Using .github/agents/code-reviewer.md, review [filename]
```

---

## 📋 Quick Reference Table

| Agent | Use Case | Quick Command (Copilot) |
|-------|----------|-------------------------|
| code-reviewer | PR reviews, code quality | `@workspace Use .github/agents/code-reviewer.md, review [file]` |
| bug-hunter | Find bugs and edge cases | `@workspace Use .github/agents/bug-hunter.md, scan [file]` |
| test-generator | Create test suites | `@workspace Use .github/agents/test-generator.md, generate tests for [file]` |
| performance-analyzer | Performance issues | `@workspace Use .github/agents/performance-analyzer.md, analyze [file]` |
| security-auditor | Security audit | `@workspace Use .github/agents/security-auditor.md, audit [file]` |
| documentation-writer | Add docs | `@workspace Use .github/agents/documentation-writer.md, document [file]` |
| code-optimizer | Optimize code | `@workspace Use .github/agents/code-optimizer.md, optimize [file]` |

---

## 🎨 Best Practices

### 1. Be Specific
❌ Bad:
```
Review my code
```

✅ Good:
```
@workspace Using .github/agents/code-reviewer.md, review UserViewModel.kt
focusing on:
- Coroutine cancellation handling
- State management with Flow
- Error handling patterns
```

### 2. Combine Agents
```
@workspace First use .github/agents/bug-hunter.md to find issues in
ProductRepository.kt, then use .github/agents/code-optimizer.md to
suggest improvements.
```

### 3. Scope Your Request
```
@workspace Using .github/agents/security-auditor.md, audit only the
authentication-related functions in UserRepository.kt
```

### 4. Request Specific Format
```
@workspace Using .github/agents/code-reviewer.md, review ProductScreen.kt
and provide results in a table format with severity, location, and fix.
```

---

## 🚀 Workflow Examples

### Pre-Commit Workflow
```bash
# 1. Review changes
git diff --name-only | while read file; do
    echo "Reviewing $file with Copilot..."
done

# In Copilot Chat:
@workspace Review all changed files using .github/agents/code-reviewer.md
```

### Pre-PR Workflow
```bash
# Terminal
echo "Files to review:"
git diff main --name-only

# In Copilot Chat:
@workspace Using .github/agents/code-reviewer.md and
.github/agents/security-auditor.md, review all files changed in this branch
```

### Bug Fix Workflow
```bash
# 1. Find the bug
# In Copilot:
@workspace Use .github/agents/bug-hunter.md to analyze [file]
for the reported [issue]

# 2. Generate tests
@workspace Use .github/agents/test-generator.md to create tests
that reproduce this bug

# 3. Verify fix
@workspace Use .github/agents/code-reviewer.md to review my fix
```

### New Feature Workflow
```bash
# 1. Implement feature
# [write code]

# 2. Generate tests
@workspace Use .github/agents/test-generator.md for [FeatureClass]

# 3. Add documentation
@workspace Use .github/agents/documentation-writer.md for [FeatureClass]

# 4. Review
@workspace Use .github/agents/code-reviewer.md to review the complete feature

# 5. Security check
@workspace Use .github/agents/security-auditor.md to audit the new feature
```

---

## 🔄 Automation (Advanced)

### GitHub Actions Integration

Create `.github/workflows/ai-review.yml`:
```yaml
name: AI Code Review

on: pull_request

jobs:
  ai-review:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4

      - name: Review with AI
        run: |
          # Use GitHub Copilot API or other AI service
          # Reference agent files for review guidelines

      - name: Post results
        uses: actions/github-script@v7
        with:
          script: |
            github.rest.issues.createComment({
              issue_number: context.issue.number,
              owner: context.repo.owner,
              repo: context.repo.repo,
              body: 'AI Review Results: ...'
            })
```

### VS Code Task

Create `.vscode/tasks.json`:
```json
{
  "version": "2.0.0",
  "tasks": [
    {
      "label": "AI Code Review",
      "type": "shell",
      "command": "code --goto .github/agents/code-reviewer.md",
      "problemMatcher": []
    }
  ]
}
```

---

## 💡 Tips & Tricks

### Tip 1: Agent Chaining
```
@workspace
1. Use bug-hunter.md to find issues
2. Use code-optimizer.md to suggest fixes
3. Use test-generator.md to create tests for the fixes
```

### Tip 2: Custom Agent Modifications
Create project-specific variations:
```bash
cp .github/agents/code-reviewer.md .github/agents/code-reviewer-custom.md
# Edit to add project-specific rules
```

### Tip 3: Context Management
For large files:
```
@workspace Using .github/agents/performance-analyzer.md,
analyze only the data loading functions (lines 100-200) in [file]
```

### Tip 4: Save Favorite Prompts
Create quick reference:
```bash
echo "@workspace Using .github/agents/code-reviewer.md, review " > ~/copilot-review.txt
```

---

## 🎓 Learning Resources

- [GitHub Copilot Documentation](https://docs.github.com/en/copilot)
- [Claude API Documentation](https://docs.anthropic.com/)
- [OpenAI API Documentation](https://platform.openai.com/docs)

---

## 🆘 Troubleshooting

**Issue**: Agent not working as expected
- **Solution**: Make sure to reference the full path: `.github/agents/[name].md`

**Issue**: Response too generic
- **Solution**: Be more specific in your request and include file context

**Issue**: Can't find the agent file
- **Solution**: Check that agents are in `.github/agents/` directory

**Issue**: Agent instructions too long
- **Solution**: Reference specific sections: "Use the Security section from code-reviewer.md"

---

## 📝 Contributing

To improve these agents:
1. Edit the agent file in `.github/agents/`
2. Test with different AI tools
3. Update this guide if you find better usage patterns
4. Share your findings with the team