---
title:  Repository Pattern và Unit of Work
tags: ['Design Pattern']
---

## Generic Repository Pattern

```csharp
public interface IRepository<T, K> where T : class
{
    Task<IQueryable<T>> GetAll(Expression<Func<T, bool>> filter = null, 
            Func<IQueryable<T>, IOrderedQueryable<T>> orderBy = null,
            Func<IQueryable<T>, IIncludableQueryable<T, object>> include = null,
            bool disabledTracking = true);
    T GetById(K id);
    Task<T> GetByIdAsync(K id);
    void Add(T entity);
    Task<T> AddAsync(T entity);
    void Update(T entity);
    Task<T> UpdateAsync(T entity);
    void Remove(T entity);
    Task<T> RemoveAsync(T entity);
    void Remove(K id);
    Task<T> RemoveAsync(K id);
    void RemoveRange(List<T> entities);
    bool Exists(Expression<Func<T, bool>> filter);
}
```

```csharp
public class Repository<T, K> : IDisposable, IRepository<T, K> where T : DomainEntity<T>
{
    private readonly AppDbContext _context;
    public Repository(AppDbContext context)
    {
        _context = context;
    }
    public void Add(T entity)
    {
        _context.Set<T>().Add(entity);
    }

    public async Task<T> AddAsync(T entity)
    {
        var entity = await _context.Set<T>().AddAsync(entity);
        return entity;
    }

    async TaskIQueryable<T>> IRepository<T, K>.GetAll(Expression<Func<T, bool>> filter, 
        Func<IQueryable<T>, IOrderedQueryable<T>> orderBy, 
        Func<IQueryable<T>, IIncludableQueryable<T, object>> include,
        bool disabledTracking)
    {
        IQueryable<T> items = _context.Set<T>();

        if (disabledTracking)
        {
            items = items.AsNoTracking();
        }

        if (filter != null)
        {
            items = items.Where(filter);
        }

        if (include != null)
        {
            items = include(items);
        }

        if (orderBy != null)
        {
            items = orderBy(items);
        }

        return items;
    }

    public T GetById(K id)
    {
        return _context.Set<T>().Find(id);
    }

    public async Task<T> GetByIdAsync(K id)
    {
        var item = await _context.Set<T>().FindAsync(id);
        return item;
    }

    public void Remove(T entity)
    {
        if (_context.Entry(entity).State == EntityState.Detached)
        {
            _context.Set<T>().Attach(entity);
        }
        _context.Set<T>().Remove(entity);
    }

    public async Task<T> RemoveAsync(T entity)
    {
        if (_context.Entry(entity).State == EntityState.Detached)
        {
            _context.Set<T>().Attach(entity);
        }
        var entity = await _context.Set<T>().RemoveAsync(entity);
        return entity;
    }

    public void Remove(K id)
    {
        Remove(GetById(id));
    }

    public async Task<T> RemoveAsync(K id)
    {
        var entity = await RemoveAsync(GetById(id));
        return entity;
    }

    public void RemoveRange(List<T> entities)
    {
        _context.Set<T>().RemoveRange(entities);
    }

    public void Update(T entity)
    {
        _context.Set<T>().Attach(entity);
        _context.Entry(entity).State = EntityState.Modified;
    }

    public async Task<T> UpdateAsync(T entity)
    {
        _context.Set<T>().Attach(entity);
        _context.Entry(entity).State = EntityState.Modified;
        return entity;
    }

    public bool Exists(Expression<Func<T, bool>> filter)
    {
        return _context.Set<T>().Any(filter);
    }

    private bool disposed = false;
    public void Dispose()
    {
        Dispose(true);
        GC.SuppressFinalize(this);
    }

    private void Dispose(bool disposing)
    {
        if (!disposed)
        {
            if (disposing)
            {
                _context.Dispose();
            }
        }
        disposed = true;
    }
}
```

## Unit of Work Pattern

```cs
public interface IUnitOfWork
{
    IGenericRepository<T, K> GenericRepository<T, K>() where T : class
    void Commit();
}
```

```cs
public class UnitOfWork : IUnitOfWork, IDisposable
{
    private readonly AppDbContext _context;

    public UnitOfWork(AppDbContext context)
    {
        _context = context;
    }

    public IGenericRepository<T, K> GenericRepository<T, K>() where T : class
    {
        IGenericRepository<T, K> repository = new GenericRepository<T, K>(_context);
        return repository;
    }

    public void Commit()
    {
        _context.SaveChanges();
    }

    private bool disposed = false;
    public void Dispose()
    {
        Dispose(true);
        GC.SuppressFinalize(this);
    }

    private void Dispose(bool disposing)
    {
        if (!disposed)
        {
            if (disposing)
            {
                _context.Dispose();
            }
        }
        disposed = true;
    }
}
```
