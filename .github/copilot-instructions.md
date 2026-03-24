# GitHub Copilot Custom Instructions

This workspace has specialized AI agents for different code analysis tasks. When working on this project:

## Available Agents

You can invoke specialized agents by referencing their files:

### Code Review
Reference: `.github/agents/code-reviewer.md`
Use for: PR reviews, code quality checks, best practices

### Code Optimization
Reference: `.github/agents/code-optimizer.md`
Use for: Performance improvements, refactoring suggestions

### Bug Hunting
Reference: `.github/agents/bug-hunter.md`
Use for: Finding potential bugs, edge cases, correctness issues

### Test Generation
Reference: `.github/agents/test-generator.md`
Use for: Creating unit tests, integration tests, UI tests

### Documentation
Reference: `.github/agents/documentation-writer.md`
Use for: KDoc comments, README files, architecture docs

### Performance Analysis
Reference: `.github/agents/performance-analyzer.md`
Use for: Performance bottlenecks, memory issues, optimization

### Security Audit
Reference: `.github/agents/security-auditor.md`
Use for: Security vulnerabilities, OWASP compliance

## Usage Examples

**Example 1: Code Review**
```
@workspace Using .github/agents/code-reviewer.md guidelines,
review the changes in UserViewModel.kt
```

**Example 2: Generate Tests**
```
Reference .github/agents/test-generator.md and create comprehensive
tests for ProductRepository.kt
```

**Example 3: Security Audit**
```
Using the security-auditor agent specifications,
audit the authentication flow in LoginScreen.kt
```

## Project Context

- **Project Type**: Kotlin Multiplatform (KMP) with Compose Multiplatform
- **Architecture**: MVVM with Repository pattern
- **Database**: Room for KMP
- **UI**: Compose Multiplatform (Android + iOS)
- **Platforms**: Android, iOS

## Code Standards

- Use Kotlin idioms and best practices
- Follow Compose guidelines for state management
- Ensure proper coroutine and Flow usage
- Maintain thread safety and avoid memory leaks
- Write tests for all new features
- Document public APIs with KDoc

## When Suggesting Code

1. Check agent files for specific guidelines
2. Follow project architecture patterns
3. Ensure KMP compatibility (avoid platform-specific APIs in common code)
4. Consider both Android and iOS implications
5. Provide complete, runnable code examples