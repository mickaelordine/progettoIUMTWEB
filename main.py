
A=[['0', '0', '1', '0', '0'], ['0', '1', '0', '1', '0'], ['1', '1', '1', '1', '1'], ['1', '0', '0', '0', '1'], ['1', '0', '0', '0', '1']]


# 90 degree pattern
def get_pattern_90():
    matrix = []
    for j in range(cols):
        A.append()
        for i in range(rows):
            A[i].append(A[i][j])
    print(A)
    return A


rows, cols = (5, 5)


